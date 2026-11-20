// Favor ViewModel
package cn.primeessence.garden

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

import javax.inject.Inject
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

@HiltViewModel
class FavorViewModel @Inject constructor(
    private val repo: FavorRepo,
    private val noteRepo: NoteRepo
) : ViewModel() {
    
    private val okHttpClient = okhttp3.OkHttpClient()
    
    private val _favorList = MutableStateFlow<List<LiteFavor>>(emptyList())
    val favorList: StateFlow<List<LiteFavor>> = _favorList.asStateFlow()

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _proc = MutableStateFlow<String>("init")
    val proc: StateFlow<String> = _proc.asStateFlow()

    private val _updating = MutableStateFlow<String>("init")
    val updating: StateFlow<String> = _updating.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _favorList.value = repo.getLatest()
        }
    }

    fun add() {
        val cateId: Long = 0L
        val noteId: Long = 0L
        val title: String = ""
        val content: String = ""
        
        viewModelScope.launch {
            try {
                repo.insert(
                    Favor(
                        cateId = cateId,
                        noteId = noteId,
                        title = title,
                        content = content
                    )
                )
            } catch (e: Exception) {
                
            }
        }
    }
    
    suspend fun fetchHtml(url: String): String = withContext(Dispatchers.IO) {
        return@withContext try {
            _proc.value = "start fetch html"
            val req = Request.Builder()
                .url(url)
                .get()
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                //.header("Accept-Encoding", "gzip, deflate, br")
                .header("Connection", "keep-alive")
                .header("Upgrade-Insecure-Requests", "1")
                .header("Sec-Fetch-Dest", "document")
                .header("Sec-Fetch-Mode", "navigate")
                .header("Sec-Fetch-Site", "none")
                .header("Sec-Fetch-User", "?1")
                .build()
            val resp = okHttpClient.newCall(req).execute()
            if (resp.isSuccessful) {
                _proc.value = "success"
                val bytes = resp.body?.bytes()
                bytes?.toString(Charsets.UTF_8) ?: "empty"
            } else {
                _proc.value = "failed"
                "failed"
            }
        } catch (e: Exception) {
            _proc.value = "exception"
            "exception"
        }
    }
    
    fun grab(favorId: Long) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val favor = repo.getById(favorId)
                if(favor != null && favor.noteId > 0) {
                    _updating.value = "favor is not null"
                    val note = noteRepo.getById(favor.noteId)
                    if (note != null) {
                        _updating.value = "note is ok"
                        val html = note.content.trim()
                        var newContent = "init"
                        if (html.isBlank()) {
                            newContent = "null: " + html
                            _updating.value = "null" + html
                        } else {
                            _updating.value = "start parse html"
                            val doc = Jsoup.parse(html)
                            
                            val plat = doc.select("div#subscribe-wrapper").text()
                            val author = doc.select("span.author").text()
                            val uptime = doc.select("div.summary span.time").text()
                            
                            //val editorList = doc.select("div.edit-info-line")
                            val numsA = doc.select("span.section-count").text().trim()
                            val numsB = doc.select("#open_sections_list").text().trim()
                            val nums = if(numsA.isBlank()) numsB else numsA
                            val info = doc.select("div.info-detail").text()
                            
                            val videoList = mutableListOf<String>()
                            //val liListA = doc.select(".sections-list > li")
                            //val liListB = doc.select(".sec-setions > li")
                            //val liList = if(liListA.isEmpty()) liListB else liListA
                            
                            val liList = doc.select(".sec-bd li")
                            
                            liList.forEach { li ->
                                val a = li.selectFirst("a") ?: return@forEach
                                val href = a.attr("href")
                                val tit = a.selectFirst("h1.title")?.text() ?: ""
                                val tim = a.selectFirst("span.time")?.text() ?: ""
                                val lineText = "${tim.trim()}|${tit.trim()}\n${href.trim()}"
                                videoList.add(lineText)
                            }
                            val videoText = videoList.joinToString("\n")
                            
                            newContent = buildString {
                                append("平台：")
                                append(plat)
                                append("\n")
                                append("作者：")
                                append(author)
                                append("\n")
                                append("时间：")
                                append(uptime)
                                append("\n")
                                append("期数：")
                                append(nums)
                                append("\n")
                                append("简介: ")
                                append(info)
                                append("\n")
                                append("列表")
                                append("\n")
                                append(videoText)
                                append("\n")
                            }
                        }
                        _updating.value = "save new content"
                        val targetItem = favor.copy(content = newContent)
                        repo.update(targetItem)
                    } else {
                        _updating.value = "url is error"
                    }
                } else {
                    _updating.value = "favor is null"
                }
            } catch (ex: Exception) {
                _updating.value = "grab exception"
                //ex.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }
    
    fun fetchVideo(favorId: Long) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val favor = repo.getById(favorId)
                if(favor != null) {
                    _updating.value = "favor is not null"
                    val content = favor.content.trim()
                    val lines = content.split("\n").map {it.trim()}
                    val urls = mutableListOf<String>()
                    for(line in lines) {
                        if(line.startsWith("https://")){
                            urls.add(line) 
                        }
                    }
                    val videoList = mutableListOf<String>()
                    if(urls.isNotEmpty()){
                        for(url in urls) {
                            val html = fetchHtml(url)
                            noteRepo.insert(
                                Note(
                                    title = favor.title,
                                    content = html,
                                    enabled = 1
                                )
                            )
                            val doc = Jsoup.parse(html)
                            val videoSrc = doc.selectFirst("video")?.attr("src") ?: ""
                            if(videoSrc.isNotEmpty()){ videoList.add(videoSrc) }
                        }
                    }
                    _updating.value = "save video list"
                    val targetContent = videoList.joinToString("\n")
                    repo.insert(
                        Favor(
                            cateId = 1023L,
                            noteId = favor.noteId,
                            title = favor.title,
                            content = targetContent,
                            enabled = 1
                        )
                    )
                } else {
                    _updating.value = "favor is null"
                }
            } catch (ex: Exception) {
                _updating.value = "grab exception"
            } finally {
                _loading.value = false
            }
        }
    }

    fun deleteById(favorId: Long) {
        viewModelScope.launch {
            try {
                
                repo.deleteById(favorId)
                
            } catch (ex: Exception) {
                
            }
        }
    }
}
