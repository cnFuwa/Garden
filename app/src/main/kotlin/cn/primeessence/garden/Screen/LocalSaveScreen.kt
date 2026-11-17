// LocalSave Screen
package cn.primeessence.garden

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
@Composable
fun LocalSaveScreen(
) {
    val vm: LocalSaveViewModel = hiltViewModel()
    val context = LocalContext.current
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    var fileName by remember { mutableStateOf("note.txt") }
    var subFolderName by remember { mutableStateOf("GardenBackup") }
    var textContent by remember { mutableStateOf("测试文本内容\n第二行内容") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = subFolderName,
            onValueChange = { subFolderName = it },
            label = { Text("Download下子文件夹（可不填）") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = fileName,
            onValueChange = { fileName = it },
            label = { Text("文件名") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        OutlinedTextField(
            value = textContent,
            onValueChange = { textContent = it },
            label = { Text("文本内容") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            maxLines = 8
        )

        Button(
            onClick = {
                // 末尾补斜杠，满足MediaStore RELATIVE_PATH规范
                val folderArg = if (subFolderName.isNotBlank()) "$subFolderName/" else ""
                vm.saveTextToDownload(
                    fileName = fileName,
                    content = textContent,
                    subFolder = folderArg
                )
            },
            modifier = Modifier.padding(top = 16.dp),
            enabled = uiState !is SaveUiState.Loading
        ) {
            Text("保存到Download")
        }

        when (uiState) {
            SaveUiState.Idle -> {}
            SaveUiState.Loading -> {
                Text(
                    text = "保存中...",
                    modifier = Modifier.padding(top = 12.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            is SaveUiState.Success -> {
                val uri = (uiState as SaveUiState.Success).uri
                Text(
                    text = "✅ 保存成功\nUri: $uri",
                    modifier = Modifier.padding(top = 12.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
                vm.resetState()
            }
            is SaveUiState.Failed -> {
                val msg = (uiState as SaveUiState.Failed).message
                Text(
                    text = "❌ 失败：$msg",
                    modifier = Modifier.padding(top = 12.dp),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                vm.resetState()
            }
        }
    }
}
