// NoteEdit ViewModel
package cn.primeessence.garden

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteEditViewModel @Inject constructor(
    private val repo: NoteRepo,
    private val application: Application
) : ViewModel() {

    private val _note = MutableStateFlow<Note?>(null)
    val note: StateFlow<Note?> = _note.asStateFlow()

    // fun checkFont(): Boolean{
    //     val result = checkFontHasCodePoint(
    //         ctx = application,
    //         fontResId = R.font.mi_san,
    //         codePoint = 0x21334
    //     )
    //     return result
    // }

    fun load(id: Long) {
        viewModelScope.launch {
            _note.value = repo.getById(id)
        }
    }

    fun import(){
        val fileNam: String = "/unihan/pinyin.txt"
        viewModelScope.launch {
            repo.importAssetsFile(
                application,
                fileName
            )
        }
    }
    
    fun reset(id: Long) {
        viewModelScope.launch {
            _note.value = repo.getById(id)
        }
    }

    fun updateCateId(cateId: Long) {
        _note.update { old ->
            old?.copy(cateId = cateId)
        }
    }

    fun updateTitle(title: String) {
        _note.update { old ->
            old?.copy(title = title)
        }
    }

    fun updateContent(content: String) {
        _note.update { old ->
            old?.copy(content = content)
        }
    }
    
    fun updateEnabled(enabled: Int) {
        _note.update { old ->
            old?.copy(enabled = enabled)
        }
    }
    
    fun updateIsDeleted(isDeleted: Int) {
        _note.update { old ->
            old?.copy(isDeleted = isDeleted)
        }
    }

    fun submit(
        cateId: Long,
        title: String,
        content: String,
        enabled: Int,
        isDeleted: Int
    ) {

        viewModelScope.launch {
            try {
                _note.update { old ->
                    old?.copy(
                        cateId = cateId,
                        title = title,
                        content = content,
                        enabled = enabled,
                        isDeleted = isDeleted
                    )
                }
                val target = _note.value
                if(target != null){
                    repo.update(target)
                }                
            } catch (ex: Exception) {
                // ex.printStackTrace()
            }
        }
    }
}
