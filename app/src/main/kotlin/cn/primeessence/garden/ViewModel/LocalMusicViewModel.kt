package cn.primeessence.garden

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalMusicViewModel @Inject constructor() : ViewModel() {
    private var mediaController: MediaController? = null

    private val _musicList = MutableStateFlow<List<LocalMusic>>(emptyList())
    val musicList: StateFlow<List<LocalMusic>> = _musicList.asStateFlow()

    private val _currentPlayIndex = MutableStateFlow<Int>(-1)
    val currentPlayIndex: StateFlow<Int> = _currentPlayIndex.asStateFlow()

    fun connectService(context: Context) {
        val sessionToken = SessionToken(context, ComponentName(context, PlaybackService::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
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
                e.printStackTrace()
            }
        }, MoreExecutors.directExecutor())
    }

    fun loadLocalMusic(context: Context) {
        viewModelScope.launch {
            val list = queryLocalAudio(context)
            _musicList.value = list
        }
    }

    fun playListFromIndex(musicList: List<LocalMusic>, startIndex: Int) {
        val mediaItems = musicList.map { music ->
            MediaItem.fromUri(music.uri)
        }
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
}

fun queryLocalAudio(context: Context): List<LocalMusic> {
    val list = mutableListOf<LocalMusic>()
    val projection = arrayOf(
        android.provider.MediaStore.Audio.Media._ID,
        android.provider.MediaStore.Audio.Media.TITLE,
        android.provider.MediaStore.Audio.Media.ARTIST
    )
    val cursor = context.contentResolver.query(
        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        projection,
        android.provider.MediaStore.Audio.Media.IS_MUSIC + " = 1",
        null,
        android.provider.MediaStore.Audio.Media.DATE_ADDED + " DESC"
    )
    cursor?.use {
        while (it.moveToNext()) {
            val id = it.getLong(0)
            val title = it.getString(1) ?: "未知歌曲"
            val artist = it.getString(2) ?: "未知歌手"
            val uri = Uri.withAppendedPath(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id.toString())
            list.add(LocalMusic(id, title, artist, uri))
        }
    }
    return list
}
