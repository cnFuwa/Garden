// Meta ViewModel
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
class MetaViewModel @Inject constructor(
    private val repo: MetaRepo
) : ViewModel() {

    private val _metaList = MutableStateFlow<List<LiteMeta>>(emptyList())
    val metaList: StateFlow<List<LiteMeta>> = _metaList.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _metaList.value = repo.getLatest()
        }
    }

    fun create() {
        val title: String = ""
        val content: String = ""
        
        viewModelScope.launch {
            try {
                repo.insert(
                    Meta(
                        title = title,
                        content = content
                    )
                )
            } catch (e: Exception) {
                
            }
        }
    }

    fun deleteById(metaId: Long) {
        viewModelScope.launch {
            try {
                
                repo.deleteById(metaId)
                
            } catch (ex: Exception) {
                
            }
        }
    }
}
