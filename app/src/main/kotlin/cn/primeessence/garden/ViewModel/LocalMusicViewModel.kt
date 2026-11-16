package cn.primeessence.garden

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalMusicViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    private var mediaController: MediaController? = null

    private val _musicList = MutableStateFlow<List<LocalMusic>>(emptyList())
    val musicList: StateFlow<List<LocalMusic>> = _musicList.asStateFlow()

    private val _currentPlayIndex = MutableStateFlow<Int>(-1)
    val currentPlayIndex: StateFlow<Int> = _currentPlayIndex.asStateFlow()

    private val prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    init {
        // 重启 App 后恢复上次授权的音乐目录
        prefs.getString(KEY_TREE_URI, null)?.let { saved ->
            try {
                loadFromTreeUri(Uri.parse(saved))
            } catch (e: Exception) {
                //e.printStackTrace()
            }
        }
    }

    fun connectService() {
        val sessionToken = SessionToken(appContext, ComponentName(appContext, PlaybackService::class.java))
        val controllerFuture = MediaController.Builder(appContext, sessionToken).buildAsync()
        controllerFuture.addListener({
            try {
                mediaController = controllerFuture.get()
                mediaController?.addListener(object : Player.Listener {
                    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                        val index = mediaController?.currentMediaItemIndex ?: -1
                        _currentPlayIndex.value = index
                    }
                })
            } catch (e: Exception) {
                //e.printStackTrace()
            }
        }, MoreExecutors.directExecutor())
    }

    fun onTreeSelected(treeUri: Uri) {
        
        try {
            appContext.contentResolver.takePersistableUriPermission(
                treeUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        } catch (e: Exception) {
            //e.printStackTrace()
        }
        prefs.edit().putString(KEY_TREE_URI, treeUri.toString()).apply()
        loadFromTreeUri(treeUri)
    }
private fun loadFromTreeUri(treeUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val root = DocumentFile.fromTreeUri(appContext, treeUri)
            if (root == null || !root.canRead()) {
                _musicList.value = emptyList()
                return@launch
            }

            val files = mutableListOf<DocumentFile>()
            collectAudioFiles(root, files)

            val list = files.mapIndexed { index, file ->
                val fallbackName = stripExtension(file.name ?: "未知歌曲")
                val (title, artist) = readMetadata(file.uri, fallbackName)
                LocalMusic(
                    id = index.toLong(),
                    title = title,
                    artist = artist,
                    uri = file.uri
                )
            }
            _musicList.value = list
        }
    }

    private fun collectAudioFiles(folder: DocumentFile, out: MutableList<DocumentFile>) {
        val children = folder.listFiles() ?: return
for (child in children) {
            if (child.isDirectory) {
                collectAudioFiles(child, out)
            } else if (isAudioFile(child)) {
                out.add(child)
            }
        }
    }

    private fun isAudioFile(file: DocumentFile): Boolean {
        val mime = file.type
        if (mime != null && mime.startsWith("audio/")) return true
        val name = file.name ?: return false
        return AUDIO_EXTENSIONS.any { name.endsWith(it, ignoreCase = true) }
    }

    private fun readMetadata(uri: Uri, fallbackName: String): Pair<String, String> {
        var title = fallbackName
        var artist = "未知歌手"
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(appContext, uri)
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                ?.takeIf { it.isNotBlank() }?.let { title = it }
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                ?.takeIf { it.isNotBlank() }?.let { artist = it }
} catch (e: Exception) {
            // 无法解析元数据时使用文件名兜底
        } finally {
            try {
                retriever.release()
            } catch (_: Exception) {
            }
        }
        return title to artist
    }

    private fun stripExtension(name: String): String {
        val dot = name.lastIndexOf('.')
        return if (dot > 0) name.substring(0, dot) else name
    }

    fun playListFromIndex(musicList: List<LocalMusic>, startIndex: Int) {
        val mediaItems = musicList.map { music -> MediaItem.fromUri(music.uri) }
        mediaController?.setMediaItems(mediaItems, startIndex, 0)
        mediaController?.prepare()
        mediaController?.play()
    }

    fun pause() {
        mediaController?.pause()
    }

    fun play() {
        mediaController?.play()
    }

    fun seekTo(index: Int, positionMs: Long = 0) {
        mediaController?.seekTo(index, positionMs)
    }

    override fun onCleared() {
        mediaController?.release()
        mediaController = null
        super.onCleared()
    }

    companion object {
        private const val PREFS_NAME = "saf_music_prefs"
        private const val KEY_TREE_URI = "key_tree_uri"
        private val AUDIO_EXTENSIONS = listOf(
            ".mp3", ".m4a", ".flac", ".wav", ".ogg", ".aac", ".opus", ".wma"
        )
    }
}