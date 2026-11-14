package cn.primeessence.garden

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemButton
import androidx.compose.material3.ListItemButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import cn.primeessence.garden.LocalMusicViewModel
import com.google.accompanist.permissions.PermissionStatus.isGranted

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocalMusicScreen(
    gotoBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vm: LocalMusicViewModel = hiltViewModel()
    val musicList by vm.musicList.collectAsStateWithLifecycle()
    val currentIndex by vm.currentPlayIndex.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val audioPermission = Manifest.permission.READ_MEDIA_AUDIO
    val permissionState = rememberPermissionState(audioPermission)

    LaunchedEffect(Unit) {
        vm.connectService(context)
    }

    LaunchedEffect(permissionState) {
        
        if (permissionState.status.isGranted) {
            vm.loadLocalMusic(context)
        } else {
            permissionState.launchPermissionRequest()
        }
    }

    Column(
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
                    ListItemButton(
                        onClick = { vm.playListFromIndex(musicList, index) },
                        colors = if (index == currentIndex) {
                            ListItemButtonDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        } else {
                            ListItemButtonDefaults.colors()
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
