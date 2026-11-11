// MateEdit
package cn.primeessence.garden

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

import androidx.compose.runtime.mutableStateOf

import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontWeight

@Composable
fun MateEdit(
    mateId: Long,
    gotoBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vm: MateEditViewModel = hiltViewModel()

    val mate by vm.mate.collectAsStateWithLifecycle()

    val targetId: Long = maxOf(0, mateId)

    LaunchedEffect(targetId) {
        vm.load(targetId)
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
            text = "Edit Mate ${targetId}",
            style = MaterialTheme.typography.headlineSmall
        )
        
        val item = mate 
        if(item != null) {

            var cateIdText by remember(item.id) { mutableStateOf(item.cateId.toString()) }
            var title by remember(item.id) { mutableStateOf(item.title) }
            var content by remember(item.id) { mutableStateOf(item.content) }
            var enabledText by remember(item.id) { mutableStateOf(item.enabled.toString()) }
            var isDeletedText by remember(item.id) { mutableStateOf(item.isDeleted.toString()) }

            OutlinedTextField(
                value = cateIdText,
                onValueChange = { cateIdText = it },
                label = { Text("cateId") },
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("title") },
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("content") },
                singleLine = false,
                minLines = 3,
                maxLines = 5,
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = TextStyle(
                    fontFamily = misanHan
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )
            
            OutlinedTextField(
                value = enabledText,
                label = { Text("enabled") },
                onValueChange = { enabledText = it },
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            OutlinedTextField(
                value = isDeletedText,
                label = { Text("isDeleted") },
                onValueChange = { isDeletedText = it },
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                
                Button(
                    onClick = {
                        gotoBack()
                    }
                ) {
                    Text("back")
                }

                Button(
                    onClick = {
                        val cateId: Long = cateIdText.toLongOrNull() ?: 0L
                        vm.submit(
                            cateId = cateId,
                            title = title,
                            content = content,
                            enabled = enabledText.toIntOrNull() ?: 0,
                            isDeleted = isDeletedText.toIntOrNull() ?: 0
                        )
                    }
                ) {
                    Text("save")
                }

                Button(
                    onClick = { vm.reset(item.id) }
                ) {
                    Text("reset")
                }
            }
        } else {
            Text(
                text = "note not found",
                style = MaterialTheme.typography.titleSmall
            )    
        }
    }
}
