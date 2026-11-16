package cn.primeessence.garden

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LocalMusicScreen(
    gotoBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vm: LocalMusicViewModel = hiltViewModel()
    val musicList by vm.musicList.collectAsStateWithLifecycle()
    val currentIndex by vm.currentPlayIndex.collectAsStateWithLifecycle()

    val treeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { treeUri ->
        treeUri?.let { vm.onTreeSelected(it) }
    }

    LaunchedEffect(Unit) {
        vm.connectService()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "本地音乐",
            modifier = Modifier.padding(12.dp)
        )

        if (musicList.isEmpty()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("尚未选择音乐文件夹，授权后将读取该目录及子目录中的音频")
Button(
                    onClick = { treeLauncher.launch(null) },
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Text("选择音乐文件夹")
                }
            }
        } else {
            LazyColumn {
                itemsIndexed(musicList) { index, music ->
                    val interactionSource = remember { MutableInteractionSource() }
                    ListItem(
                        headlineContent = { Text(music.title) },
                        supportingContent = { Text(music.artist) },
                        modifier = Modifier.clickable(
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