package cn.primeessence.garden

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.HorizontalDivider

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
    gotoNoteList: () -> Unit,
    gotoMusicList: () -> Unit,
    gotoMetaList: () -> Unit
) {
    val vm: HomeViewModel = hiltViewModel()
    
    val metaMap by vm.metaMap.collectAsStateWithLifecycle()
    val countMap by vm.countMap.collectAsStateWithLifecycle()

    val loading by vm.loading.collectAsStateWithLifecycle()
    val updating by vm.updating.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.load()
    }
    
    val noteMaxNum = metaMap["note"] ?: 0L
    val musicMaxNum = metaMap["music"] ?: 0L
    val metaMaxNum = metaMap["meta"] ?: 0L

    val noteNum = countMap["note"] ?: 0L
    val musicNum = countMap["music"] ?: 0L
    val metaNum = countMap["meta"] ?: 0L

    val scrollState = rememberScrollState()

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
        
        HorizontalDivider(
            modifier = Modifier.padding(
                vertical = 8.dp
            )
        )

        Text(
            text = "Note: total = ${noteNum}, MaxNum = ${noteMaxNum}"
        )
        
        Text(
            text = "Music: total = ${musicNum}, MaxNum: ${musicMaxNum}"
        )
        Text(
            text = "Meta: total = ${metaNum}, MaxNum: ${metaMaxNum}"
        )

        HorizontalDivider(
            modifier = Modifier.padding(
                vertical = 8.dp
            )
        )
        
        // Note
        Row(
            modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Button(
                onClick = { gotoNoteList() }
            ){
                
                Text(
                    text = "Note"
                )
            }
            
            Button(
                onClick = { vm.addNote() },
                enabled = (noteNum < noteMaxNum)
            ){
                
                Text(
                    text = "Add"
                )
            }
        }
        
        Row(
            modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Button(
                onClick = { vm.loadNote() },
                enabled = !loading
            ){
                
                Text(
                    text = "Load"
                )
            }
            
            Button(
                onClick = { vm.clear("note") },
                enabled = !updating
            ){
                
                Text(
                    text = "Clear"
                )
            }
        }
        
        HorizontalDivider(
            modifier = Modifier.padding(
                vertical = 8.dp
            )
        )
        
        // Music
        Row(
            modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Button(
                onClick = { gotoMusicList() }
            ){
                
                Text(
                    text = "Music"
                )
            }
            
            Button(
                onClick = { vm.addMusic() },
                enabled = (musicNum < musicMaxNum)
            ){
                
                Text(
                    text = "Add"
                )
            }
        }
        Row(
            modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Button(
                onClick = { vm.loadMusic() },
                enabled = !loading
            ){
                
                Text(
                    text = "Load"
                )
            }
            
            Button(
                onClick = { vm.clear("music") },
                enabled = !updating
            ){
                
                Text(
                    text = "Clear"
                )
            }
        }

        // Meta
        Row(
            modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Button(
                onClick = { gotoMetaList() }
            ){
                
                Text(
                    text = "Meta"
                )
            }
            
            Button(
                onClick = { vm.addMeta() },
                enabled = (metaNum < metaMaxNum)
            ){
                
                Text(
                    text = "Add"
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(
                vertical = 8.dp
            )
        )

    }
}
