// Read ViewModel
package cn.primeessence.garden

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

import javax.inject.Inject

data class TxtFileItem(
    val name: String,
    val uri: Uri
)

@HiltViewModel
class ReadViewModel @Inject constructor(
    
): ViewModel() {
    private val _txtList = MutableStateFlow<List<TxtFileItem>>(emptyList())
    val txtList: StateFlow<List<TxtFileItem>> = _txtList.asStateFlow()

    private val _content = MutableStateFlow<String>("")
    val content: StateFlow<String> = _content.asStateFlow()

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    fun scanFolder(context: Context, treeUri: Uri) {
        viewModelScope.launch {
            _loading.value = true
            val result = withContext(Dispatchers.IO) {
                val list = mutableListOf<TxtFileItem>()
                val root = DocumentFile.fromTreeUri(context, treeUri) ?: return@withContext emptyList()
                traverse(context, root, list)
                list
            }
            _txtList.value = result
            _loading.value = false
        }
    }

    private fun traverse(context: Context, doc: DocumentFile, outList: MutableList<TxtFileItem>) {
        if (doc.isDirectory) {
            doc.listFiles().forEach { child ->
                traverse(context, child, outList)
            }
        } else {
            val name = doc.name ?: return
            if (name.endsWith(".txt", ignoreCase = true)) {
                outList.add(TxtFileItem(name, doc.uri))
            }
        }
    }

    fun readTxt(context: Context, uri: Uri) {
        viewModelScope.launch {
            _loading.value = true
            val text = withContext(Dispatchers.IO) {
                buildString {
                    context.contentResolver.openInputStream(uri)?.use { input ->
                        BufferedReader(InputStreamReader(input)).use { reader ->
                            var line: String?
                            while (reader.readLine().also { line = it } != null) {
                                appendLine(line)
                            }
                        }
                    }
                }
            }
            _content.value = text
            _loading.value = false
        }
    }

    fun clearContent() {
        _content.value = ""
    }
}
