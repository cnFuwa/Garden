// Mate ViewModel
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
class MateViewModel @Inject constructor(
    private val repo: MateRepo
) : ViewModel() {

    private val _mateList = MutableStateFlow<List<Mate>>(emptyList())
    val mateList: StateFlow<List<Mate>> = _mateList.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _mateList.value = repo.getLatest()
        }
    }

    fun create() {
        val cateId: Long = 0L
        val title: String = ""
        val content: String = ""
        
        viewModelScope.launch {
            try {
                repo.insert(
                    Mate(
                        cateId = cateId,
                        title = title,
                        content = content
                    )
                )
            } catch (e: Exception) {
                
            }
        }
    }

    fun deleteById(mateId: Long) {
        viewModelScope.launch {
            try {
                
                repo.deleteById(mateId)
                
            } catch (ex: Exception) {
                
            }
        }
    }
}
