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
    private val mateRepo: MateRepo,
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
        getMateTotal()
        getMetaTotal()
    }
    
    fun getMetaList(){
        viewModelScope.launch {
            try {
                val metaItems: List<LiteMeta> = metaRepo.getLatest()
                var noteMaxNum: Long = AppNumItem.NOTE_MAX_NUM
                var mateMaxNum: Long = AppNumItem.MATE_MAX_NUM
                var musicMaxNum: Long = AppNumItem.MUSIC_MAX_NUM
                var metaMaxNum: Long = AppNumItem.META_MAX_NUM
            
                for(item in metaItems){
                    val title = item.title.trim()
                    val num = item.content.trim().toLongOrNull() ?: 0L
                    when(title) {
                        "note" -> noteMaxNum =  maxOf(noteMaxNum, num)
                        "mate" -> mateMaxNum =  maxOf(mateMaxNum, num)
                        "music" -> musicMaxNum =  maxOf(musicMaxNum, num)
                        "meta" -> metaMaxNum =  maxOf(metaMaxNum, num)
                    }
                }
                _metaMap.value = mapOf(
                    "note" to noteMaxNum,
                    "mate" to mateMaxNum,
                    "music" to musicMaxNum,
                    "meta" to metaMaxNum
                )
            
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
    
    fun getMateTotal() {
        viewModelScope.launch {
            try {
                val total: Long = mateRepo.getTotal()
                _countMap.value = _countMap.value.toMutableMap().apply {
                    put("mate", total)
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
                if(total < available) {
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
    
    fun addMate() {
        val metaMap = _metaMap.value
        val available: Long = metaMap["mate"] ?: AppNumItem.MATE_MAX_NUM
        viewModelScope.launch {
            try {
                val total: Long  = mateRepo.getTotal()
                if(total < available) {
                    mateRepo.insert(
                        Mate(
                            title = MateItem.TITLE,
                            cateId = MateItem.CATE_ID,
                            content = MateItem.CONTENT,
                            enabled = MateItem.ENABLED,
                            isDeleted = MateItem.IS_DELETED
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
                if(total < available) {
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
                if(total < available) {
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
                val theItem: Note? = noteRepo.getById(1L)
                if(theItem == null) return@launch
                
                val content: String = theItem.content.trim()
                if(content.isEmpty()) return@launch
                
                val lines = content.split("\n").map{ it.trim() }
                
                for(line in lines){
                    val item = line.split("、").map{ it.trim() }
                    if (item.size < 3) continue
                    val tag = item[0]
                    val title = item[1]
                    val fn = item[2]
                    if(fn.isEmpty()) continue
                    val cateId: Long = when(tag) {
                        "xueke" -> NoteCateItem.XUEKE
                        "hanzi" -> NoteCateItem.HANZI
                        "guoxue" -> NoteCateItem.GUOXUE
                        "numeric" -> NoteCateItem.NUMERIC
                        "variant" -> NoteCateItem.VARIANT
                        "readx" -> NoteCateItem.READX
                        else -> 2011L
                    }
                    val fileName: String = "docs/$fn"
                    noteRepo.loadAssetsFile(
                        application,
                        fileName,
                        title,
                        cateId
                    )
                }
                
            } catch (e: Exception) {
            
            } finally {
                getNoteTotal()
                _loading.value = false
            }
        }
    }

    fun loadMate(){
        if(_loading.value) return
        viewModelScope.launch {
            _loading.value = true

            try{
                val theItem: Mate? = mateRepo.getById(1L)
                if(theItem == null) return@launch
                
                val content: String = theItem.content.trim()
                if(content.isEmpty()) return@launch
                
                val lines = content.split("\n").map{ it.trim() }
                
                for(line in lines){
                    val item = line.split("、").map{ it.trim() }
                    if (item.size < 3) continue
                    val tag = item[0]
                    val title = item[1]
                    val fn = item[2]
                    if(fn.isEmpty()) continue
                    
                    val cateId: Long = when(tag) {
                        "person" -> MateCateItem.PERSON
                        "history" -> MateCateItem.HISTORY
                        "shenhua" -> MateCateItem.SHENHUA
                        "modern" -> MateCateItem.MODERN
                        "star" -> MateCateItem.STAR
                        "creator" -> MateCateItem.CREATOR
                        else -> 2011L
                    }
                    val fileName: String = "person/$fn"
                    mateRepo.loadAssetsFile(
                        application,
                        fileName,
                        title,
                        cateId
                    )
                }
                
            } catch (e: Exception) {
            
            } finally {
                getMateTotal()
                _loading.value = false
            }
        }
    }

    fun loadMusic() {
        if(_loading.value) return
        viewModelScope.launch {
            _loading.value = true

            try{
                val theItem: Music? = musicRepo.getById(1L)
                if(theItem == null) return@launch
                
                val content: String = theItem.content.trim()
                if(content.isEmpty()) return@launch
                
                val lines = content.split("\n").map{ it.trim() }
                
                for(line in lines){
                    val item = line.split("、").map{ it.trim() }
                    if (item.size < 3) continue
                    val tag = item[0]
                    val title = item[1]
                    val fn = item[2]
                    if(fn.isEmpty()) continue
                    
                    val cateId: Long = when(tag) {
                        "music" -> MusicCateItem.MUSIC
                        "classic" -> MusicCateItem.CLASSIC
                        "children" -> MusicCateItem.CHILDREN
                        "young" -> MusicCateItem.YOUNG
                        "work" -> MusicCateItem.WORK
                        "soft" -> MusicCateItem.SOFT
                        else -> 2011L
                    }
                    val fileName: String = "music/$fn"
                    musicRepo.loadAssetsFile(
                        application,
                        fileName,
                        title,
                        cateId
                    )
                }
                
            } catch (e: Exception) {
            
            } finally {
                getMusicTotal()
                _loading.value = false
            }
        }
    }
    
    fun loadMeta() {
        if(_loading.value) return
        viewModelScope.launch {
            _loading.value = true

            try{
                val theItem: Meta? = metaRepo.getById(1L)
                if(theItem == null) return@launch
                
                val content: String = theItem.content.trim()
                if(content.isEmpty()) return@launch
                
                val lines = content.split("\n").map{ it.trim() }
                
                for(line in lines){
                    val item = line.split("、").map{ it.trim() }
                    if (item.size < 3) continue
                    val tag = item[0]
                    val title = item[1]
                    val fn = item[2]
                    if(fn.isEmpty()) continue
                    
                    val fileName: String = "meta/$fn"
                    metaRepo.loadAssetsFile(
                        application,
                        fileName,
                        title
                    )
                }
                
            } catch (e: Exception) {
            
            } finally {
                getMetaTotal()
                _loading.value = false
            }
        }
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
                    "${getWord(cp)}(U+%04X)".format(cp)
                }
                add(item)
            }
        }
        return codeList.joinToString("\n")
    }
    
    fun clear(cate: String) {
        when (cate) {
             "note" -> clearNote()
             "mate" -> clearMate()
             "music" -> clearMusic()
        }
    }
    
    fun clearNote() {
        if(_updating.value == true) return
        viewModelScope.launch {
            _updating.value == true
            val noteItem: Note? = noteRepo.getById(2L)
            var cateId: Long = 0L
            if(noteItem != null) {
                cateId = noteItem.content.trim().toLongOrNull() ?: 0L
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
                        title = "",
                        content = "",
                        enabled = 0
                    )
                    noteRepo.update(targetNote)
                    idx += 1
                }
            } catch (e: Exception) {
                
            } finally {
                _updating.value == false
            }
        }
    }
    
    fun clearMate() {
        if(_updating.value == true) return
        viewModelScope.launch {
            _updating.value == true
            val mateItem: Mate? = mateRepo.getById(2L)
            var cateId: Long = 0L
            if(mateItem != null) {
                cateId = mateItem.content.trim().toLongOrNull() ?: 0L
            }
            
            if(cateId <= 10L) return@launch
    
            try {
                val idList = mateRepo.getIds(cateId)
                var idx: Int = 1
                for(mateId in idList){
                    val mate: Mate? = mateRepo.getById(mateId)
                    if (mate == null) continue
                    val targetMate = Mate(
                        id = mateId,
                        cateId = 1,
                        title = "",
                        content = "",
                        enabled = 0
                    )
                    mateRepo.update(targetMate)
                    idx += 1
                }
            } catch (e: Exception) {
                
            } finally {
                _updating.value == false
            }
        }
    }
    
    fun clearMusic() {
        if(_updating.value == true) return
        viewModelScope.launch {
            _updating.value == true
            val musicItem: Music? = musicRepo.getById(2L)
            var cateId: Long = 0L
            if(musicItem != null) {
                cateId = musicItem.content.trim().toLongOrNull() ?: 0L
            }
            
            if(cateId <= 10L) return@launch

            try {
                val idList = musicRepo.getIds(cateId)
                var idx: Int = 1
                for(musicId in idList){
                    val music: Music? = musicRepo.getById(musicId)
                    if (music == null) continue
                    val targetMusic = Music(
                        id = musicId,
                        cateId = 1,
                        title = "",
                        content = "",
                        enabled = 0
                    )
                    musicRepo.update(targetMusic)
                    idx += 1
                }
            } catch (e: Exception) {
                
            } finally {
                _updating.value == false
            }
        }
    }
}
