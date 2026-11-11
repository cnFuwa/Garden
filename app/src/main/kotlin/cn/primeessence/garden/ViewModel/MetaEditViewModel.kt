// MetaEdit ViewModel
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
class MetaEditViewModel @Inject constructor(
    private val repo: MetaRepo,
) : ViewModel() {

    private val _meta = MutableStateFlow<Meta?>(null)
    val meta: StateFlow<Meta?> = _meta.asStateFlow()
    
    fun load(id: Long) {
        viewModelScope.launch {
            _meta.value = repo.getById(id)
        }
    }

    fun reset(id: Long) {
        viewModelScope.launch {
            _meta.value = repo.getById(id)
        }
    }

    fun updateTitle(title: String) {
        _meta.update { old ->
            old?.copy(title = title)
        }
    }

    fun updateContent(content: String) {
        _meta.update { old ->
            old?.copy(content = content)
        }
    }
    
    fun updateEnabled(enabled: Int) {
        _meta.update { old ->
            old?.copy(enabled = enabled)
        }
    }
    
    fun updateIsDeleted(isDeleted: Int) {
        _meta.update { old ->
            old?.copy(isDeleted = isDeleted)
        }
    }

    fun submit(
        title: String,
        content: String,
        enabled: Int,
        isDeleted: Int
    ) {
        viewModelScope.launch {
            try {
                _meta.update { old ->
                    old?.copy(
                        title = title,
                        content = content.trim(),
                        enabled = enabled,
                        isDeleted = isDeleted
                    )
                }
                val target = _meta.value
                if(target != null){
                    repo.update(target)
                }                
            } catch (ex: Exception) {
                // ex.printStackTrace()
            }
        }
    }
}