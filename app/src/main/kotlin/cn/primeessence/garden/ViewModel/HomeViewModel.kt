// Home ViewModel
package cn.primeessence.garden

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteRepo: NoteRepo,
    private val application: Application
) : ViewModel() {

    private val _metaMap = MutableStateFlow<Map<String, Long>>(emptyMap())
    val metaMap: StateFlow<Map<String, Long>> = _metaMap.asStateFlow()

    private val _importing = MutableStateFlow<Boolean>(false)
    val importing: StateFlow<Boolean> = _importing.asStateFlow()

    fun load() {
        _metaMap.value = mutableMapOf(
            "note" to 128L
        )
    }
    
    fun createNote() {
        val metaMap = _metaMap.value
        val available: Long = metaMap["note"] ?: 128L
        viewModelScope.launch {
            try {
                val total: Long  = noteRepo.getTotal()
                if(total >= available) {
                    return@launch
                } else {
                    noteRepo.insert(
                        Note(
                            title = NoteItem.TITLE,
                            cateId = NoteItem.CATE_ID,
                            content = NoteItem.CONTENT,
                            enabled = NoteItem.ENABLED,
                            isDeleted = NoteItem.IS_DELETED
                        )
                    )
                }
            } catch (e: Exception) {

            }
        }
    }
    
    fun import(){
        val fileName: String = "unihan/pinyin.txt"
        if(_importing.value) return
        viewModelScope.launch {
            _importing.value = true
            try{
                repo.importAssetsFile(
                    application,
                    fileName
                )
            } catch (e: Exception) {
            
            } finally {
                _importing.value = false
            }
        }
    }

}