// FavorList
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
fun FavorList(
    gotoFavorEdit:(Long) -> Unit,
    gotoFavorRead:(Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val vm: FavorViewModel = hiltViewModel()

    val favorList by vm.favorList.collectAsStateWithLifecycle()
    val loading by vm.loading.collectAsStateWithLifecycle()
    val proc by vm.proc.collectAsStateWithLifecycle()
    val updating by vm.updating.collectAsStateWithLifecycle()
    
    var targetId: Long by remember{ mutableStateOf(0L) }
    
    var canLoad: Boolean = if(targetId>0 && !loading) {true} else {false}

    LaunchedEffect(Unit) {
        vm.load()
    }
    val procText = proc
    val updatingText = updating
    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = "Favor List ${targetId}",
            style = MaterialTheme.typography.headlineSmall
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            
            Text(
                text = "read",
                color = AppColor.CHINA_BLUE,
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
                .clickable {
                    gotoFavorRead(targetId)
                }
            )
            
            Text(
                text = "edit",
                color = AppColor.CHINA_BLUE,
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
                .clickable {
                    gotoFavorEdit(targetId)
                }
            )
            
            Text(
                text = "grab",
                color = if (canLoad) AppColor.CHINA_BLUE else Color.Gray,
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
                .clickable(enabled = canLoad) {
                    vm.grab(targetId)
                }
            )
            
            Text(
                text = "grab",
                color = if (canLoad) AppColor.CHINA_BLUE else Color.Gray,
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
                .clickable(enabled = canLoad) {
                    vm.fetchVideo(targetId)
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("proc: $procText")
            Text("updating: $updatingText")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(favorList) { item ->
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
                        text = "${item.id} [${item.cateId}]${item.title} ${if(item.enabled==1) "✅" else "❌"}",
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
