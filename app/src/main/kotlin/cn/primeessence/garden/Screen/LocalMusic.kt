package cn.primeessence.garden

import android.Manifest
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MusicListPage() {
    val vm: LocalMusicViewModel = hiltViewModel()
    val musicList by vm.musicList.collectAsStateWithLifecycle()
    val currentIndex by vm.currentPlayIndex.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val audioPermission = Manifest.permission.READ_MEDIA_AUDIO
    val permissionState = rememberPermissionState(audioPermission)

    // 连接播放器服务
    LaunchedEffect(Unit) {
        vm.connectService(context)
    }

    // 权限逻辑：授予后加载音乐
    LaunchedEffect(permissionState.status) {
        if (permissionState.status.isGranted) {
            vm.loadLocalMusic(context)
        } else {
            permissionState.launchPermissionRequest()
        }
    }

    androidx.compose.foundation.layout.Column(
        modifier = Modifier.fillMaxSize()
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
                    // ✅ M3：ListItemButton 负责点击 + 选中色
                    ListItemButton(
                        onClick = { vm.playListFromIndex(musicList, index) },
                        colors = if (index == currentIndex) {
                            androidx.compose.material3.ListItemButtonDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        } else {
                            androidx.compose.material3.ListItemButtonDefaults.colors()
                        }
                    ) {
                        ListItem(
                            headlineContent = { Text(music.title) },
                            supportingContent = { Text(music.artist) }
                        )
                    }
                }
            }
        }
    }
}
