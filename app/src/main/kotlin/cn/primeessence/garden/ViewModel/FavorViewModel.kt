// Favor ViewModel
package cn.primeessence.garden

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavorViewModel @Inject constructor(
    private val repo: FavorRepo
) : ViewModel() {

    private val _favorList = MutableStateFlow<List<LiteFavor>>(emptyList())
    val favorList: StateFlow<List<LiteFavor>> = _favorList.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _favorList.value = repo.getLatest()
        }
    }

    fun add() {
        val cateId: Long = 0L
        val startUrl: String = ""
        val title: String = ""
        val content: String = ""
        
        viewModelScope.launch {
            try {
                repo.insert(
                    Favor(
                        cateId = cateId,
                        startUrl = startUrl,
                        title = title,
                        content = content
                    )
                )
            } catch (e: Exception) {
                
            }
        }
    }

    fun deleteById(favorId: Long) {
        viewModelScope.launch {
            try {
                
                repo.deleteById(favorId)
                
            } catch (ex: Exception) {
                
            }
        }
    }
}
