package cn.primeessence.garden

import androidx.compose.foundation.clickable
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun Read(
    gotoBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vm: ReadViewModel = hiltViewModel()
    val context = LocalContext.current
    val txtList by vm.txtList.collectAsStateWithLifecycle()
    val content by vm.content.collectAsStateWithLifecycle()
    val loading by vm.loading.collectAsStateWithLifecycle()

    val pickFolderLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        uri ?: return@rememberLauncherForActivityResult
        vm.scanFolder(context, uri)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Button(onClick = { pickFolderLauncher.launch(null) }) {
                Text("Select(SAF)")
            }
            
            Button(
                onClick = { gotoBack() }
            ) {
                Text("Back")
            }
            
            Text(text = "txt number：${txtList.size}", modifier = Modifier.padding(vertical = 8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f)
            ) {
                items(txtList) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .clickable{ vm.readTxt(context, item.uri) }
                    ) {
                        Text(
                            text = item.name
                        )
                    }
                }
            }

            Text("===== txt content =====", modifier = Modifier.padding(top = 8.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f)
                    .padding(top = 4.dp)
            ) {
                item {
                    Text(text = content, modifier = Modifier.padding(4.dp))
                }
            }
        }
        
        
        
    }
}
