package cn.primeessence.garden.Screen

import android.Manifest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import cn.primeessence.garden.LocalMusicViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocalMusicScreen() {
    val vm: LocalMusicViewModel = hiltViewModel()
    val musicList by vm.musicList.collectAsStateWithLifecycle()
    val currentIndex by vm.currentPlayIndex.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val audioPermission = Manifest.permission.READ_MEDIA_AUDIO
    val permissionState = rememberPermissionState(
        permission = audioPermission,
        onPermissionResult = { granted ->
            if (granted) {
                vm.loadLocalMusic(context)
            }
        }
    )

    LaunchedEffect(Unit) {
        vm.connectService(context)
        // 安全判断：不使用 .isGranted 扩展，直接判断类型
        if (permissionState.status !is PermissionStatus.Granted) {
            permissionState.launchPermissionRequest()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "本地音乐列表，点击歌曲开始播放整个列表",
            modifier = Modifier.padding(12.dp)
        )
        if (musicList.isEmpty()) {
            Text("暂无本地音乐，请授予读取音频权限", modifier = Modifier.padding(12.dp))
        } else {
            LazyColumn {
                itemsIndexed(musicList) { index, music ->
                    val interactionSource = remember { MutableInteractionSource() }
                    ListItem(
                        headlineContent = { Text(music.title) },
                        supportingContent = { Text(music.artist) },
                        modifier = Modifier
                            .clickable(
                                interactionSource = interactionSource,
                                indication = ripple(),
                                onClick = { vm.playListFromIndex(musicList, index) }
                            ),
                        colors = if (index == currentIndex) {
                            ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        } else {
                            ListItemDefaults.colors()
                        }
                    )
                }
            }
        }
    }
}
