// MetaList
package cn.primeessence.garden

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun MetaList(
    gotoMetaEdit:(Long) -> Unit,
    gotoMetaRead:(Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val vm: MetaViewModel = hiltViewModel()

    val metaList by vm.metaList.collectAsStateWithLifecycle()
    var targetId: Long by remember{ mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        vm.load()
    }

    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = "Meta List ${targetId}",
            style = MaterialTheme.typography.headlineSmall
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    if(targetId > 0){
                        gotoMetaRead(targetId)
                    }
                }
            ) {
                Text("read")
            }

            Button(
                onClick = {
                    if(targetId > 0){
                        gotoMetaEdit(targetId)
                    }
                }
            ) {
                Text("edit")
            }

            Button(
                onClick = {
                    if(targetId > 0){
                        vm.deleteById(targetId)
                    }
                }
            ) {
                Text("delete")
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(metaList) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            targetId = item.id
                        },
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${item.id} ${item.title} ${if(item.enabled==1) ✅}",
                        style = MaterialTheme.typography.titleSmall,
                        color = if (targetId == item.id) {
                            Color(0xFF2196F3)
                        } else {
                            Color.Unspecified
                        }
                    )
                }
            }
        }
            
        HorizontalDivider(
            modifier = Modifier.padding(
                vertical = 8.dp
            )
        )

        Text(
            text = "the bottom line",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
