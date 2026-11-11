// MateRead
package cn.primeessence.garden

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
//import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontWeight

@Composable
fun MateRead(
    mateId: Long,
    gotoBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vm: MateReadViewModel = hiltViewModel()

    val mate by vm.mate.collectAsStateWithLifecycle()

    val id: Long = maxOf(0, mateId)
    LaunchedEffect(id) {
        vm.load(id)
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Read Mate $id",
            style = MaterialTheme.typography.headlineSmall
        )
        
        val item = mate
        if(item != null) {
            Text(
                text = "Id: ${item.id}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "title: ${item.title}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "cateId: ${item.cateId}",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "content:\n${item.content}",
                fontFamily = misanHan,
                lineHeight = 24.sp
            )
            
            Text(
                text = "enabled: ${item.enabled}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "isDeleted: ${item.isDeleted}",
                style = MaterialTheme.typography.bodyLarge
            )

        } else {
            Text(
                text = "mate not found",
                style = MaterialTheme.typography.bodyLarge
            )    
        }
        
        Button(onClick = {
            gotoBack()
        }) {
            Text("Back")
        }
    }
}
