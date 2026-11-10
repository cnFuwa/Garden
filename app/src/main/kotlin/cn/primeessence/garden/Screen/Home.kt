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
    
    val noteMap by vm.noteMap.collectAsStateWithLifecycle()

    val loading by vm.loading.collectAsStateWithLifecycle()
    val updating by vm.updating.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.load()
    }
    
    val scrollState = rememberScrollState()
    val noteMaxNum = metaMap["note"] ?: 0L
    
    val hanziCateId: Long = 101L
    val numericCateId: Long = 102L
    val variantCateId: Long = 103L
    val wordToCodeCateId: Long = 104L
    val readingCateId: Long = 105L
    
    val numericNum = noteMap[numericCateId] ?: 0L
    val hanziNum = noteMap[hanziCateId] ?: 0L
    val variantNum = noteMap[variantCateId] ?: 0L
    val wordToCodeNum = noteMap[wordToCodeCateId] ?: 0L
    val readingNum = noteMap[readingCateId] ?: 0L
    
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
            text = "noteMaxNum: ${noteMaxNum} wordToCodeNum: ${wordToCodeNum}"
        )
        
        Text(
            text = "hanziNum: ${hanziNum} numericNum: ${numericNum}"
        )
        Text(
            text = "variantNum: ${variantNum} readingNum: ${readingNum}"
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
            onClick = { vm.update("pinyin") }
        ){
            Text(
                text = "savePinyin"
            )
        }
        
        Button(
            onClick = { vm.update("wordToCode") }
        ){
            Text(
                text = "UpdateWordToCode"
            )
        }
        
        
        if(numericNum == 0L){
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
        }
        
        
        if(hanziNum == 0L){
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
        }
        
        
        if(variantNum == 0L){
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
        }
        
        
        if(readingNum == 0L){
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
}
