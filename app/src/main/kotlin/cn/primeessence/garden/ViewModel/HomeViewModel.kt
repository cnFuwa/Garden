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
    private val musicRepo: MusicRepo,
    private val metaRepo: MetaRepo,
    private val application: Application
) : ViewModel() {

    private val _metaMap = MutableStateFlow<Map<String, Long>>(emptyMap())
    val metaMap: StateFlow<Map<String, Long>> = _metaMap.asStateFlow()

    private val _countMap = MutableStateFlow<Map<String, Long>>(emptyMap())
    val countMap: StateFlow<Map<String, Long>> = _countMap.asStateFlow()

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _updating = MutableStateFlow<Boolean>(false)
    val updating: StateFlow<Boolean> = _updating.asStateFlow()

    fun load() {
        getMetaList()
        getNoteTotal()
        getMusicTotal()
        getMetaTotal()
    }
    
    fun getMetaList(){
        viewModelScope.launch {
            try {
                val metaItems: List<Meta> = metaRepo.getLatest()

                var noteMaxNum: Long = AppNumItem.NOTE_MAX_NUM
                var musicMaxNum: Long = AppNumItem.MUSIC_MAX_NUM
                var metaMaxNum: Long = AppNumItem.META_MAX_NUM
            
                if(metaItems.isNotEmpty){
                    for(item in metaItems){
                        val title = item.title.trim()
                        val num = item.content.trim().toLongOrNull() ?: 0L
                        when(title) {
                            "note" -> noteMaxNum =  maxOf(noteMaxNum, num)
                            "music" -> musicMaxNum =  maxOf(musicMaxNum, num)
                            "meta" -> metaMaxNum =  maxOf(metaMaxNum, num)
                        }
                    }

                    _countMap.value = _countMap.value.toMutableMap().apply {
                        put("note", noteMaxNum)
                        put("music", musicMaxNum)
                        put("meta", MetaMaxNum)
                    }
                }
                
            } catch (e: Exception) {

            } finally {

            }
        }
    }
    
    fun getNoteTotal() {
        viewModelScope.launch {
            try {
                val total: Long = noteRepo.getTotal()
                _countMap.value = _countMap.value.toMutableMap().apply {
                    put("note", total)
                }
            } catch (e: Exception) {

            } finally {

            }
        }
    }
    
    fun getMusicTotal() {
        viewModelScope.launch {
            try {
                val total: Long = musicRepo.getTotal()
                _countMap.value = _countMap.value.toMutableMap().apply {
                    put("music", total)
                }
            } catch (e: Exception) {

            } finally {

            }
        }
    }
    
    fun getMetaTotal() {
        viewModelScope.launch {
            try {
                val total: Long = metaRepo.getTotal()
                _countMap.value = _countMap.value.toMutableMap().apply {
                    put("meta", total)
                }
            } catch (e: Exception) {

            } finally {

            }
        }
    }
    
    fun addNote() {
        val metaMap = _metaMap.value
        val available: Long = metaMap["note"] ?: AppNumItem.NOTE_MAX_NUM
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
    
    fun addMusic() {
        val metaMap = _metaMap.value
        val available: Long = metaMap["music"] ?: AppNumItem.MUSIC_MAX_NUM
        viewModelScope.launch {
            try {
                val total: Long  = musicRepo.getTotal()
                if(total >= available) {
                    return@launch
                } else {
                    musicRepo.insert(
                        Music(
                            title = MusicItem.TITLE,
                            cateId = MusicItem.CATE_ID,
                            content = MusicItem.CONTENT,
                            enabled = MusicItem.ENABLED,
                            isDeleted = MusicItem.IS_DELETED
                        )
                    )
                }
            } catch (e: Exception) {

            }
        }
    }

    fun addMeta() {
        val metaMap = _metaMap.value
        val available: Long = metaMap["meta"] ?: AppNumItem.META_MAX_NUM
        viewModelScope.launch {
            try {
                val total: Long  = metaRepo.getTotal()
                if(total >= available) {
                    return@launch
                } else {
                    metaRepo.insert(
                        Meta(
                            title = MetaItem.TITLE,
                            content = MetaItem.CONTENT,
                            enabled = MetaItem.ENABLED,
                            isDeleted = MetaItem.IS_DELETED
                        )
                    )
                }
            } catch (e: Exception) {

            }
        }
    }

    fun loadNote(){
        if(_loading.value) return
        viewModelScope.launch {
            _loading.value = true

            try{
                val noteItem: Note? = noteRepo.getById(1L)
                if(noteItem == null) return@launch
                
                val content: String = noteItem.content.trim()
                if(content.isEmpty()) return@launch
                
                val lines = content.split("\n").map{ it.trim() }
                
                for(line in lines){
                    val item = line.split(";").map{ it.trim() }
                    if (item.size < 2) continue
                    val tag = item[0]
                    val fn = item[1]
                    val cateId: Long = when(tag) {
                        "hanzi" -> CateIdItem.HANZI
                        "guoxue" -> CateIdItem.GUOXUE
                        "numeric" -> CateIdItem.NUMERIC
                        "variant" -> CateIdItem.VARIANT
                        "readx" -> CateIdItem.READX
                        else -> 2001L
                    }
                    val fileName: String = "docs/$fn"
                    noteRepo.loadAssetsFile(
                        application,
                        fileName,
                        cateId
                    )
                }
                getNoteTotal()
            } catch (e: Exception) {
            
            } finally {
                _loading.value = false
            }
        }
    }
    
    fun loadMusic() {
        
    }
    
    fun getCodePoint(hexStr: String): Int{
        return try {
            hexStr.toInt(16)
        } catch(e: NumberFormatException) {
            0
        }
    }
    
    fun getWord(codePoint: Int): String {
        if (codePoint <= 0) return ""
        return try {
            String(Character.toChars(codePoint))
        } catch(e: IllegalArgumentException){
            ""
        }
    }
    
    fun getCodePointFromText(text: String): String {
        val codeList = buildList<String> {
            var i = 0
            while (i < text.length) {
                val cp = text.codePointAt(i)
                i += Character.charCount(cp)
                val item = if (cp == 32 || cp == 12288) {
                    " "
                } else {
                    "${getHanzi(cp)}(U+%04X)".format(cp)
                }
                add(item)
            }
        }
        return codeList.joinToString("\n")
    }
    
    fun clear(cate: String) {
        when (cate) {
             "note" -> clearNote()
             "music" -> clearMusic()
        }
    }
    
    fun clearMusic() {}
    
    fun clearNote() {
        if(_updating.value == true) return
        viewModelScope.launch {
            _updating.value == true
            val metaItem: Meta? = metaRepo.getById(1L)
            var cateId: Long = 0L
            if(metaItem != null) {
                cateId = metaItem.content.trim().toLongOrNull() ?: 0L
            }
            
            if(cateId <= 10L) return@launch
    
            try {
                val idList = noteRepo.getIds(cateId)
                var idx: Int = 1
                for(noteId in idList){
                    val note: Note? = noteRepo.getById(noteId)
                    if (note == null) continue
                    val targetNote = Note(
                        id = noteId,
                        cateId = 1,
                        title = "clear $idx",
                        content = "",
                        enabled = 0
                    )
                    noteRepo.update(targetNote)
                    idx += 1
                }
                _updating.value == false
            } catch (e: Exception) {
                
            } finally {
                _updating.value == false
            }
        }
    }
    
}
