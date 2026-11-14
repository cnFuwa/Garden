package cn.primeessence.garden

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.rememberPermissionState

@Composable
fun MusicListPage() {
    val vm: LocalMusicViewModel = hiltViewModel()
    val musicList by vm.musicList.collectAsStateWithLifecycle()
    val currentIndex by vm.currentPlayIndex.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val audioPermission = Manifest.permission.READ_MEDIA_AUDIO
    val permissionState = rememberPermissionState(audioPermission)

    LaunchedEffect(Unit) {
        vm.connectService(context)
    }

    LaunchedEffect(permissionState.status.isGranted) {
        if (permissionState.status.isGranted) {
            vm.loadLocalMusic(context)
        } else {
            permissionState.launchPermissionRequest()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "本地音乐列表，点击歌曲开始播放整个列表",
            modifier = Modifier.padding(12.dp)
        )
        if (musicList.isEmpty()) {
            Text("暂无本地音乐，请授予读取音频权限", modifier = Modifier.padding(12.dp))
        } else {
            LazyColumn {
                itemsIndexed(musicList) { index, music ->
                    ListItem(
                        headlineContent = { Text(music.title) },
                        supportingContent = { Text(music.artist) },
                        selected = index == currentIndex,
                        onClick = {
                            vm.playListFromIndex(musicList, index)
                        }
                    )
                }
            }
        }
    }
}
