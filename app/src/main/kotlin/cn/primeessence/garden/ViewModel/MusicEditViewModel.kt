// MusicEdit ViewModel
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
class MusicEditViewModel @Inject constructor(
    private val repo: MusicRepo,
) : ViewModel() {

    private val _music = MutableStateFlow<Music?>(null)
    val music: StateFlow<Music?> = _music.asStateFlow()
    
    fun load(id: Long) {
        viewModelScope.launch {
            _music.value = repo.getById(id)
        }
    }

    fun reset(id: Long) {
        viewModelScope.launch {
            _music.value = repo.getById(id)
        }
    }

    fun updateCateId(cateId: Long) {
        _music.update { old ->
            old?.copy(cateId = cateId)
        }
    }

    fun updateTitle(title: String) {
        _music.update { old ->
            old?.copy(title = title)
        }
    }

    fun updateContent(content: String) {
        _music.update { old ->
            old?.copy(content = content)
        }
    }
    
    fun updateEnabled(enabled: Int) {
        _music.update { old ->
            old?.copy(enabled = enabled)
        }
    }
    
    fun updateIsDeleted(isDeleted: Int) {
        _music.update { old ->
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
                _music.update { old ->
                    old?.copy(
                        cateId = cateId,
                        title = title,
                        content = content.trim(),
                        enabled = enabled,
                        isDeleted = isDeleted
                    )
                }
                val target = _music.value
                if(target != null){
                    repo.update(target)
                }                
            } catch (ex: Exception) {
                // ex.printStackTrace()
            }
        }
    }
}