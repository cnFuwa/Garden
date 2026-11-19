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
    private val repo: FavorRepo
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
        val startUrl: String = ""
        val title: String = ""
        val content: String = ""
        
        viewModelScope.launch {
            try {
                repo.insert(
                    Favor(
                        cateId = cateId,
                        startUrl = startUrl,
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
                .header("Accept-Encoding", "gzip, deflate, br")
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
                resp.body?.string() ?: "empty"
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
                if(favor != null) {
                    _updating.value = "favor is not null"
                    val startUrl = favor.startUrl.trim()
                    if (startUrl.isNotEmpty() && startUrl.startsWith("https://")) {
                        _updating.value = "url is ok"
                        val html = fetchHtml(startUrl)
                        var newContent = "init"
                        if (
                            html == "empty"
                            || html == "failed"
                            || html=="exception"
                            || html.isBlank()
                        ) {
                            newContent = "null: " + html
                        } else {
                            val doc = Jsoup.parse(html)
                            val infoTitle = doc.select("div.info_title").text()
                            val infoDetail = doc.select("div.info_detail").text()
                            newContent = buildString {
                                append(infoTitle)
                                append("\n")
                                append(infoDetail)
                            }
                        }
                        _updating.value = newContent
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
    
    fun deleteById(favorId: Long) {
        viewModelScope.launch {
            try {
                
                repo.deleteById(favorId)
                
            } catch (ex: Exception) {
                
            }
        }
    }
}
