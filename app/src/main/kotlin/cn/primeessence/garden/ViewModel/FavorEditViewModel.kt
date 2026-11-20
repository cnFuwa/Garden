// FavorEdit ViewModel
package cn.primeessence.garden

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavorEditViewModel @Inject constructor(
    private val repo: FavorRepo,
) : ViewModel() {

    private val _favor = MutableStateFlow<Favor?>(null)
    val favor: StateFlow<Favor?> = _favor.asStateFlow()
    
    fun load(id: Long) {
        viewModelScope.launch {
            _favor.value = repo.getById(id)
        }
    }

    fun reset(id: Long) {
        viewModelScope.launch {
            _favor.value = repo.getById(id)
        }
    }

    fun updateCateId(cateId: Long) {
        _favor.update { old ->
            old?.copy(cateId = cateId)
        }
    }

    fun updateTitle(title: String) {
        _favor.update { old ->
            old?.copy(title = title.trim())
        }
    }

    fun updateNoteId(noteId: Long) {
        _favor.update { old ->
            old?.copy(noteId = noteId)
        }
    }

    fun updateContent(content: String) {
        _favor.update { old ->
            old?.copy(content = content.trim())
        }
    }
    
    fun updateEnabled(enabled: Int) {
        _favor.update { old ->
            old?.copy(enabled = enabled)
        }
    }
    
    fun updateIsDeleted(isDeleted: Int) {
        _favor.update { old ->
            old?.copy(isDeleted = isDeleted)
        }
    }
    
    
    
    fun submit(
        cateId: Long,
        title: String,
        noteId: Long,
        content: String,
        enabled: Int,
        isDeleted: Int
    ) {
        viewModelScope.launch {
            try {
                _favor.update { old ->
                    old?.copy(
                        cateId = cateId,
                        title = title.trim(),
                        noteId = noteId,
                        content = content.trim(),
                        enabled = enabled,
                        isDeleted = isDeleted
                    )
                }
                val target = _favor.value
                if(target != null){
                    repo.update(target)
                }                
            } catch (ex: Exception) {
                // ex.printStackTrace()
            }
        }
    }
}