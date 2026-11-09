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
import kotlin.text.Regex

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

    private val _updating = MutableStateFlow<Boolean>(false)
    val updating: StateFlow<Boolean> = _updating.asStateFlow()

    fun load() {
        _metaMap.value = mutableMapOf(
            "note" to 256L
        )
        getNoteCount(101L)
        getNoteCount(102L)
        getNoteCount(103L)
        getNoteCount(105L)
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
    
    fun loadHanzi(){
        loadNote("hanzi", 101L)
    }
    
    fun loadNumeric(){
        loadNote("numeric", 102L)
    }
    
    fun loadVariant(){
        loadNote("variant", 103L)
    }
    
    fun loadReading(){
        loadNote("reading", 105L)
    }
    
    
    fun getCodePoint(hexStr: String): Int{
        return try {
            hexStr.toInt(16)
        } catch(e: NumberFormatException) {
            0
        }
    }
    
    fun getHanzi(codePoint: Int): String {
        if (codePoint <= 0) return ""
        return try {
            String(Character.toChars(codePoint))
        } catch(e: IllegalArgumentException){
            ""
        }
    }

    fun renew(cate: String) {
        when (cate) {
            "numeric" -> renewNumeric()
             "variant" -> renewVariant()
             "reading" -> renewReading()
        }
    }
    
    fun renewNumeric() {
        viewModelScope.launch {
            try {
                val idList = noteRepo.getIds(102L)
                var idx: Int = 1
                for(noteId in idList){
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
        
                        val hexStr: String = code.removePrefix("U+")
                        val codePoint: Int = getCodePoint(hexStr)
                        val hanzi: String = getHanzi(codePoint)
                        
                        val newStr = buildString {
                            append(hanzi)
                            append("(")
                            append(code)
                            append(")\n")
                            append(tag)
                            append("\n")
                            append(textContent)
                        }
                        itemList.add(newStr)
                    }
                    if(itemList.isEmpty()) continue
                    val newContent = itemList.joinToString("\n\n")
                    itemList.clear()
                    val updatedNote = note.copy(
                        title = "numeric $idx [$noteId]",
                        content = newContent
                    )
                    noteRepo.update(updatedNote)
                    idx += 1
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
        
    fun renewVariant() {
        if(_updating.value == true) return
        viewModelScope.launch {
            _updating.value = true
            try {
                val idList = noteRepo.getIds(103L)
                var idx: Int = 1
                for(noteId in idList) {
                    val note: Note? = noteRepo.getById(noteId)
                    if (note == null) return@launch
        
                    val itemList = mutableListOf<String>()
                    val noteContent = note.content.trim()
                    val lines = noteContent.split("\n").map { it.trim() }
        
                    for (line in lines) {
                        if (line.isEmpty()) continue
                        val item = line.split("|").map { it.trim() }
                        if (item.size < 3) continue
        
                        val code: String = item[0]
                        val tag: String = item[1]
                        val textContent: String = item[2]
        
                        val hexStr = code.removePrefix("U+")
                        val codePoint: Int = getCodePoint(hexStr)
                        val hanzi: String = getHanzi(codePoint)

                        val variant: String = textContent.split(" ").map {
                            part ->
                                val regex = Regex("""U\+([0-9A-Fa-f]+)(?:<(.*))?""")
                                val match = regex.matchEntire(part.trim())
                                if (match != null) {
                                    val variantHexStr = match.groupValues[1]
                                    val variantSuffix = match.groupValues[2]
                                    val variantCodePoint = getCodePoint(variantHexStr)
                                    val variantHanzi = getHanzi(variantCodePoint)
                                    buildString {
                                        append(variantHanzi)
                                        append("(")
                                        append("U+")
                                        append(variantHexStr)
                                        append(") ")
                                        append(variantSuffix)
                                    }
                                } else {
                                    ""
                                }
                        }.joinToString("\n")
                        
                        val newStr = buildString {
                            append(hanzi)
                            append("(")
                            append(code)
                            append(")\n")
                            append(tag)
                            append("\n")
                            append(variant)
                        }
                        
                        itemList.add(newStr)
                    }
                    if(itemList.isEmpty()) continue
                    
                    val newContent = itemList.joinToString("\n\n")
                    itemList.clear()
                    val updatedNote = note.copy(
                        title = "variant $idx [$noteId]",
                        content = newContent
                    )
                    noteRepo.update(updatedNote)
                    idx += 1
                }
                _updating.value = false
            } catch (e: Exception) {
                // e.printStackTrace()
            } finally {
                _updating.value = false
            }
        }
    }

    fun renewReading() {
        if(_updating.value == true) return
        viewModelScope.launch {
            _updating.value = true
            try {
                val idList = noteRepo.getIds(105L)
                var idx: Int = 1
                for(noteId in idList){
                    val note: Note? = noteRepo.getById(noteId)
                    if (note == null) return@launch
        
                    val itemList = mutableListOf<String>()
                    val noteContent = note.content.trim()
                    val lines = noteContent.split("\n").map { it.trim() }
        
                    for (line in lines) {
                        if (line.isEmpty()) continue
                        val item = line.split("|").map { it.trim() }
                        if (item.size < 3) continue
        
                        val code: String = item[0]
                        val tag: String = item[1]
                        val textContent: String = item[2]
        
                        val hexStr: String = code.removePrefix("U+")
                        val codePoint: Int = getCodePoint(hexStr)
                        val hanzi: String = getHanzi(codePoint)
                        
                        val newStr = buildString {
                            append(hanzi)
                            append("(")
                            append(code)
                            append(");;")
                            append(tag)
                            append(";;")
                            append(textContent)
                        }
                        itemList.add(newStr)
                    }
                    
                    if(itemList.isEmpty()) continue
                    val newContent = itemList.joinToString("\n")
                    itemList.clear()
                    val updatedNote = note.copy(
                        title = "reading $idx [$noteId]",
                        content = newContent
                    )
                    noteRepo.update(updatedNote)
                    idx += 1
                }
                _updating.value = false
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _updating.value = false
            }
        }
    }
}
