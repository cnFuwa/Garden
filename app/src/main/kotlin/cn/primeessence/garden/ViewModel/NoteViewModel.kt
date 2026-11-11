// Note ViewModel
package cn.primeessence.garden

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.update

import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repo: NoteRepo
) : ViewModel() {

    private val _noteList = MutableStateFlow<List<Note>>(emptyList())
    val noteList: StateFlow<List<Note>> = _noteList.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _noteList.value = repo.getLatest()
        }
    }

    fun create() {
        val cateId: Long = 0L
        val title: String = ""
        val content: String = ""
        
        viewModelScope.launch {
            try {
                repo.insert(
                    Note(
                        cateId = cateId,
                        title = title,
                        content = content
                    )
                )
            } catch (e: Exception) {
                
            }
        }
    }

    fun deleteById(noteId: Long) {
        viewModelScope.launch {
            try {
                
                repo.deleteById(noteId)
                
            } catch (ex: Exception) {
                
            }
        }
    }
}
