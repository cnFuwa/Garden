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
fun Home(
    gotoNoteList: () -> Unit
) {
    val vm: HomeViewModel = hiltViewModel()
    
    val metaMap by vm.metaMap.collectAsStateWithLifecycle()

    val loading by vm.loading.collectAsStateWithLifecycle()

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
            text = "Home"
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
            onClick = { vm.createNote() }
        ){
            
            Text(
                text = "Create"
            )
        }
        
        Button(
            onClick = {
                vm.loadNumeric()
            },
            enabled = !loading
        ){
            Text(
                text = "Numeric"
            )
        }
        
        Button(
            onClick = {
                vm.loadHanzi()
            },
            enabled = !loading
        ){
            Text(
                text = "Hanzi"
            )
        }
        
        Button(
            onClick = {
                vm.loadVariant()
            },
            enabled = !loading
        ){
            Text(
                text = "Variant"
            )
        }
        
        Button(
            onClick = {
                vm.loadReading()
            },
            enabled = !loading
        ){
            Text(
                text = "Reading"
            )
        }
        
        
    }
}
