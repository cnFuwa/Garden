// Home ViewModel
package cn.primeessence.garden

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.buildString

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteRepo: NoteRepo,
    private val application: Application
) : ViewModel() {

    private val _metaMap = MutableStateFlow<Map<String, Long>>(emptyMap())
    val metaMap: StateFlow<Map<String, Long>> = _metaMap.asStateFlow()

    private val _noteMap = MutableStateFlow<Map<Long, Long>>(emptyMap())
    val noteMap: StateFlow<Map<Long, Long>> = _noteMap.asStateFlow()

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    fun load() {
        _metaMap.value = mutableMapOf(
            "note" to 256L
        )
    }
    
    fun getNoteCount(cateId: Long) {
        viewModelScope.launch {
            try {
                val total: Long = noteRepo.getCount(cateId)
                _noteMap.value = _noteMap.value.toMutableMap().apply {
                    put(cateId, total)
                }
            } catch (e: Exception) {

            } finally {

            }
        }
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
    
    fun loadNote(name: String, cateId: Long){
        val fileName: String = "unihan/$name.txt"
        if(_loading.value) return
        viewModelScope.launch {
            _loading.value = true
            try{
                noteRepo.loadAssetsFile(
                    application,
                    fileName,
                    cateId
                )
                getNoteCount(cateId)
            } catch (e: Exception) {
            
            } finally {
                _loading.value = false
            }
        }
    }
    
    fun loadNumeric(){
        loadNote("numeric", 101L)
    }
    
    fun loadHanzi(){
        loadNote("hanzi", 102L)
    }
    
    fun loadVariant(){
        loadNote("variant", 103L)
    }
    
    fun loadReading(){
        loadNote("reading", 105L)
    }
    
    fun renew(cate: String){
        if(cate == "numeric") {
            renewNumeric()
        }
        
        if(cate == "variant") {
            renewNumeric()
        }
        
        if(cate == "reading") {
            renewNumeric()
        }
    }

    fun renewNumeric() {
        viewModelScope.launch {
            try {
                val noteId: Long = 1L
                val note: Note? = noteRepo.getById(noteId)
                if (note == null) return@launch
    
                val itemList = mutableListOf<String>()
                val noteContent = note.content.trim()
                val lines = noteContent.split("\n").map { it.trim() }
    
                for (line in lines) {
                    if (line.isEmpty()) continue
                    val item = line.split(",").map { it.trim() }
                    if (item.size < 3) continue
    
                    val code: String = item[0]
                    val tag: String = item[1]
                    val textContent: String = item[2]
    
                    val hanzi = runCatching {
                        val hexStr = code.removePrefix("U+")
                        val codePoint = hexStr.toInt(16)
                        String(Character.toChars(codePoint))
                    }.getOrElse { code }
                    
                    val newStr = buildString {
                        append(hanzi)
                        append("(")
                        append(code)
                        append("),")
                        append(tag)
                        append(",")
                        append(textContent)
                    }
                    
                    itemList.add(newStr)
                }
    
                val newContent = itemList.joinToString("\n")
                val updatedNote = note.copy(content = newContent)
                noteRepo.update(updatedNote)
    
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
        
    fun renewVariant(){
        
    }
    
    fun renewReading(){
        
    }

}
