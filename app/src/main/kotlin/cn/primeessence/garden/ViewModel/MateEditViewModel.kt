// MateEdit ViewModel
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
class MateEditViewModel @Inject constructor(
    private val repo: MateRepo,
) : ViewModel() {

    private val _mate = MutableStateFlow<Mate?>(null)
    val mate: StateFlow<Mate?> = _mate.asStateFlow()
    
    fun load(id: Long) {
        viewModelScope.launch {
            _mate.value = repo.getById(id)
        }
    }

    fun reset(id: Long) {
        viewModelScope.launch {
            _mate.value = repo.getById(id)
        }
    }

    fun updateCateId(cateId: Long) {
        _mate.update { old ->
            old?.copy(cateId = cateId)
        }
    }

    fun updateTitle(title: String) {
        _mate.update { old ->
            old?.copy(title = title)
        }
    }

    fun updateContent(content: String) {
        _mate.update { old ->
            old?.copy(content = content)
        }
    }
    
    fun updateEnabled(enabled: Int) {
        _mate.update { old ->
            old?.copy(enabled = enabled)
        }
    }
    
    fun updateIsDeleted(isDeleted: Int) {
        _mate.update { old ->
            old?.copy(isDeleted = isDeleted)
        }
    }

    fun submit(
        cateId: Long,
        title: String,
        content: String,
        enabled: Int,
        isDeleted: Int
    ) {
        viewModelScope.launch {
            try {
                _mate.update { old ->
                    old?.copy(
                        cateId = cateId,
                        title = title,
                        content = content.trim(),
                        enabled = enabled,
                        isDeleted = isDeleted
                    )
                }
                val target = _mate.value
                if(target != null){
                    repo.update(target)
                }                
            } catch (ex: Exception) {
                // ex.printStackTrace()
            }
        }
    }
}