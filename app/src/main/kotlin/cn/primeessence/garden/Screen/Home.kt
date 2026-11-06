package cn.primeessence.garden

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.Button
import androidx.compose.material3.Text
//import androidx.compose.material3.HorizontalDivider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun HomeScreen(
    gotoNoteList: () -> Unit
) {
    val vm: HomeViewModel = hiltViewModel()
    
    val metaMap by vm.metaMap.collectAsStateWithLifecycle()

    val importing by vm.importing.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.load()
    }
    
    val scrollState = rememberScrollState()
    val noteNum = metaMap["note"] ?: 0L
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Text(
            text = "HomeScreen"
        )
        
        Text(
            text = "noteNum: ${noteNum}"
        )
            
        Button(
            onClick = { gotoNoteList() }
        ){
            
            Text(
                text = "NoteList"
            )
        }
        
        Button(
            onClick = {
                vm.import()
            },
            enabled = !importing
        ){
            Text(
                text = "import"
            )
        }
        
        Button(
            onClick = { vm.createNote() }
        ){
            
            Text(
                text = "Create"
            )
        }
    }
}
