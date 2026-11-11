// MetaRead ViewModel
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
class MetaReadViewModel @Inject constructor(
    private val repo: MetaRepo
) : ViewModel() {

    private val _meta = MutableStateFlow<Meta?>(null)
    val meta: StateFlow<Meta?> = _meta.asStateFlow()

    fun load(id: Long) {
        getById(id)
    }
    
    fun getById(id: Long) {
        viewModelScope.launch {
            _meta.value = repo.getById(id)
        }
    }
}
