// LoacalSave ViewModel
package cn.primeessence.garden

import android.app.Application
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.nio.charset.StandardCharsets
import javax.inject.Inject

sealed class SaveUiState {
    object Idle : SaveUiState()
    object Loading : SaveUiState()
    data class Success(val uri: Uri) : SaveUiState()
    data class Failed(val message: String) : SaveUiState()
}

@HiltViewModel
class LocalSaveViewModel @Inject constructor(
    private val application: Application
) : ViewModel{

    private val _uiState = MutableStateFlow<SaveUiState>(SaveUiState.Idle)
    val uiState: StateFlow<SaveUiState> = _uiState.asStateFlow()

    fun saveTextToDownload(
        fileName: String,
        content: String,
        subFolder: String = ""
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            _uiState.value = SaveUiState.Failed("仅支持 Android 10 (API29) 及以上")
            return
        }

        _uiState.value = SaveUiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val relativePath = if (subFolder.isNotBlank()) {
                    "${Environment.DIRECTORY_DOWNLOADS}/$subFolder"
                } else {
                    Environment.DIRECTORY_DOWNLOADS
                }

                val contentValues = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                    put(MediaStore.Downloads.MIME_TYPE, "text/plain")
                    put(MediaStore.Downloads.RELATIVE_PATH, relativePath)
                }

                val uri = getApplication<Application>().contentResolver.insert(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    contentValues
                ) ?: throw RuntimeException("无法创建文件Uri")

                val outputStream = getApplication<Application>().contentResolver.openOutputStream(uri)
                    ?: throw RuntimeException("打开输出流失败")

                outputStream.use { os ->
                    // val bom = byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte())
                    // os.write(bom)
                    os.write(content.toByteArray(StandardCharsets.UTF_8))
                }

                withContext(Dispatchers.Main.immediate) {
                    _uiState.value = SaveUiState.Success(uri)
                }

            } catch (ex: Exception) {
                withContext(Dispatchers.Main.immediate) {
                    _uiState.value = SaveUiState.Failed(ex.message ?: "未知错误")
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = SaveUiState.Idle
    }
}
