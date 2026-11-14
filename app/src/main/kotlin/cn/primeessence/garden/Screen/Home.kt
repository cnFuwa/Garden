package cn.primeessence.garden

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable

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
import androidx.compose.ui.graphics.Color

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun Home(
    gotoNoteList: () -> Unit,
    gotoMateList: () -> Unit,
    gotoMusicList: () -> Unit,
    gotoMetaList: () -> Unit,
    gotoRead: () -> Unit,
    gotoLocalMusic: () -> Unit
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
    val mateMaxNum = metaMap["mate"] ?: 0L
    val musicMaxNum = metaMap["music"] ?: 0L
    val metaMaxNum = metaMap["meta"] ?: 0L

    val noteNum = countMap["note"] ?: 0L
    val mateNum = countMap["mate"] ?: 0L
    val musicNum = countMap["music"] ?: 0L
    val metaNum = countMap["meta"] ?: 0L
    
    val canAddNote = (noteNum < noteMaxNum)
    val canAddMate = (mateNum < mateMaxNum)
    val canAddMusic = (musicNum < musicMaxNum)
    val canAddMeta = (metaNum < metaMaxNum)

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
            text = "Mate: total = ${mateNum}, MaxNum: ${mateMaxNum}"
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

            Text(
                text = "Note",
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
            )
            
            Text(
                text = "List",
                color = AppColor.CHINA_BLUE,
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
                .clickable {
                    gotoNoteList()
                }
            )

            Text(
                text = "Add",
                color = if (canAddNote) AppColor.CHINA_BLUE else Color.Gray,
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
                .clickable(enabled = canAddNote){
                    vm.addNote()
                }
            )
            
            Text(
                text = "Load",
                color = if (!loading) AppColor.CHINA_BLUE else Color.Gray,
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
                .clickable(enabled = !loading){
                    vm.loadNote()
                }
            )
            
            Text(
                text = "Clear",
                color = if (!updating) AppColor.CHINA_RED else Color.Gray,
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
                .clickable(enabled = !updating){
                    vm.clear("note")
                }
            )
            
        }

        HorizontalDivider(
            modifier = Modifier.padding(
                vertical = 8.dp
            )
        )

        // Mate
        Row(
            modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text(
                text = "Mate",
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
            )
            
            Text(
                text = "List",
                color = AppColor.CHINA_BLUE,
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
                .clickable {
                    gotoMateList()
                }
            )
            
            Text(
                text = "Add",
                color = if (canAddMate) AppColor.CHINA_BLUE else Color.Gray,
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
                .clickable(enabled = canAddMate){
                    vm.addMate()
                }
            )
            
            Text(
                text = "Load",
                color = if (!loading) AppColor.CHINA_BLUE else Color.Gray,
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
                .clickable(enabled = !loading){
                    vm.loadMate()
                }
            )
            
            Text(
                text = "Clear",
                color = if (!updating) AppColor.CHINA_RED else Color.Gray,
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
                .clickable(enabled = !updating){
                    vm.clear("mate")
                }
            )
            
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
            Text(
                text = "Music",
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
            )
            
            Text(
                text = "List",
                color = AppColor.CHINA_BLUE,
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
                .clickable {
                    gotoMusicList()
                }
            )
            
            Text(
                text = "Add",
                color = if (canAddMusic) AppColor.CHINA_BLUE else Color.Gray,
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
                .clickable(enabled = canAddMusic){
                    vm.addMusic()
                }
            )
            
            Text(
                text = "Load",
                color = if (!loading) AppColor.CHINA_BLUE else Color.Gray,
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
                .clickable(enabled = !loading){
                    vm.loadMusic()
                }
            )
            
            Text(
                text = "Clear",
                color = if (!updating) AppColor.CHINA_RED else Color.Gray,
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
                .clickable(enabled = !updating){
                    vm.clear("music")
                }
            )
            
        }

        HorizontalDivider(
            modifier = Modifier.padding(
                vertical = 8.dp
            )
        )

        // Meta
        Row(
            modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text(
                text = "Meta",
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
            )
            
            Text(
                text = "List",
                color = AppColor.CHINA_BLUE,
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
                .clickable {
                    gotoMetaList()
                }
            )
            
            Text(
                text = "Add",
                color = if (canAddMeta) AppColor.CHINA_BLUE else Color.Gray,
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
                .clickable(enabled = canAddMeta){
                    vm.addMeta()
                }
            )
            
            Text(
                text = "Load",
                color = if (!loading) AppColor.CHINA_BLUE else Color.Gray,
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
                .clickable(enabled = !loading){
                    vm.loadMeta()
                }
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(
                vertical = 8.dp
            )
        )
        
        // Action
        Row(
            modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text(
                text = "Action",
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
            )
            
            Text(
                text = "Read",
                color = AppColor.CHINA_BLUE,
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
                .clickable {
                    gotoRead()
                }
            )
            
            Text(
                text = "Music",
                color = AppColor.CHINA_BLUE,
                modifier = Modifier
                .padding(vertical=4.dp,horizontal=8.dp)
                .clickable {
                    gotoLocalMusic()
                }
            )
        }
    }
}
