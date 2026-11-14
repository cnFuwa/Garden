package cn.primeessence.garden

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cn.primeessence.garden.LocalMusicViewModel

@Composable
fun LocalMusicScreen(
    gotoBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vm: LocalMusicViewModel = hiltViewModel()
    val musicList by vm.musicList.collectAsStateWithLifecycle()
    val currentIndex by vm.currentPlayIndex.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val audioPermission = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    var hasAudioPermission by mutableStateOf(
        ContextCompat.checkSelfPermission(context, audioPermission) == PackageManager.PERMISSION_GRANTED
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasAudioPermission = granted
        if (granted) {
            vm.loadLocalMusic(context)
        }
    }

    LaunchedEffect(Unit) {
        vm.connectService(context)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "本地音乐列表，点击歌曲开始播放整个列表",
            modifier = Modifier.padding(12.dp)
        )

        if (!hasAudioPermission) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("需要读取音频权限才能加载本地音乐")

                val needRationale = remember {
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        context as androidx.activity.ComponentActivity,
                        audioPermission
                    )
                }

                if (needRationale) {
                    Text("你之前拒绝过权限，需要授予才能读取本地音频")
                    Button(onClick = { permissionLauncher.launch(audioPermission) }) {
                        Text("重新申请权限")
                    }
                } else {
                    Text("权限已被永久拒绝，系统不会再弹出申请框，请去设置手动开启")
                    Button(onClick = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    }) {
                        Text("前往应用设置")
                    }
                }
            }
        } else {
            if (musicList.isEmpty()) {
                Text("暂无本地音乐", modifier = Modifier.padding(12.dp))
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
}
