// FavorRead ViewModel
package cn.primeessence.garden

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavorReadViewModel @Inject constructor(
    private val repo: FavorRepo
) : ViewModel() {

    private val _favor = MutableStateFlow<Favor?>(null)
    val favor: StateFlow<Favor?> = _favor.asStateFlow()

    fun load(id: Long) {
        getById(id)
    }
    
    fun getById(id: Long) {
        viewModelScope.launch {
            _favor.value = repo.getById(id)
        }
    }
}
