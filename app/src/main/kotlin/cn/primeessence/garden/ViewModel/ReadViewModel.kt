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
    private val noteRepo: NoteRepo,
    private val mateRepo: MateRepo,
    private val musicRepo: MusicRepo,
    private val metaRepo: MetaRepo

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
            try {
                val trimmedLines = withContext(Dispatchers.IO) {
                    val lines = mutableListOf<String>()
                    context.contentResolver.openInputStream(uri)?.use { input ->
                        BufferedReader(InputStreamReader(input)).use { reader ->
                            var line: String?
                            var idx = 1
                            while (reader.readLine().also { line = it } != null) {
                                if(line != null){ lines.add() }
                                if(idx > 3000) break
                                idx += 1
                            }
                        }
                    }
                    lines.dropWhile { it.isBlank() }
                        .dropLastWhile { it.isBlank() }
                }
                
                if(trimmedLines.isNotEmpty()){
                    val firstLine = trimmedLines.first().trim()
                    val items = firstLine.split(";")
                    var cate = "note"
                    var cateId = AppNumItem.DRAFT_CATE_ID
                    var title = "draft"
                    if(items.size > 2){
                        cate = items[0].trim()
                        cateId = items[1].toLongOrNull() ?: AppNumItem.DRAFT_CATE_ID
                        title = items[2].trim()
                    }
                    val text = trimmedLines.joinToString("\n")
                    when(cate) {
                        "meta" -> metaRepo.insert(Meta(title = title,content = text,enabled = 1))
                        "mate" -> mateRepo.insert(Mate(cateId = cateId, title = title,content = text,enabled = 1))
                        "music" -> musicRepo.insert(Music(cateId = cateId, title = title,content = text,enabled = 1))
                        else -> noteRepo.insert(Note(cateId = cateId, title = title,content = text,enabled = 1))
                    }
                    _content.value = text
                } else {
                    _content.value = "empty text"
                }
            } catch(e: Exception) {
                
            } finally {
                _loading.value = false
            }
        }
    }
    
    fun clearContent() {
        _content.value = ""
    }
}
