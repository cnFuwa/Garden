// NoteRead
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
//import androidx.compose.ui.unit.sp

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontWeight

@Composable
fun NoteRead(
    noteId: Long,
    gotoBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vm: NoteReadViewModel = hiltViewModel()

    val note by vm.note.collectAsStateWithLifecycle()

    val id: Long = maxOf(0, noteId)
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
            text = "Read Note",
            style = MaterialTheme.typography.headlineSmall
        )
        
        val item = note
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
                text = "content: ${item.content}",
                fontFamily = misanHan
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
                text = "note not found",
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
