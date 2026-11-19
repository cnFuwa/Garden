// FilterError
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
fun FilterErrorScreen(
    gotoBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vm: FilterErrorViewModel = hiltViewModel()
    val result  by vm.result.collectAsStateWithLifecycle()
    val targetId: Long = 4L

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
            text = "Filter Error ${targetId}",
            style = MaterialTheme.typography.headlineSmall
        )
        
        val item = result 
        if(item != null) {

            var title by remember(item.id) { mutableStateOf(item.title) }
            var content by remember(item.id) { mutableStateOf(item.content) }

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
                minLines = 11,
                maxLines = 15,
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = TextStyle(
                    fontFamily = misanHan
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
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
                        vm.submit(
                            title = title,
                            content = content,
                            enabled = 1,
                            isDeleted = 0
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
                text = "item not found",
                style = MaterialTheme.typography.titleSmall
            )    
        }
    }
}
