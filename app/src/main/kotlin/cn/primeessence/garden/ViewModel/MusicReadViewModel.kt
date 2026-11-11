// MusicRead ViewModel
package cn.primeessence.garden

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicReadViewModel @Inject constructor(
    private val repo: MusicRepo
) : ViewModel() {

    private val _music = MutableStateFlow<Music?>(null)
    val music: StateFlow<Music?> = _music.asStateFlow()

    fun load(id: Long) {
        getById(id)
    }
    
    fun getById(id: Long) {
        viewModelScope.launch {
            _music.value = repo.getById(id)
        }
    }
}
