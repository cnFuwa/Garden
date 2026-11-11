// Music ViewModel
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
class MusicViewModel @Inject constructor(
    private val repo: MusicRepo
) : ViewModel() {

    private val _musicList = MutableStateFlow<List<Music>>(emptyList())
    val musicList: StateFlow<List<Music>> = _musicList.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _musicList.value = repo.getLatest()
        }
    }

    fun create() {
        val cateId: Long = 0L
        val title: String = ""
        val content: String = ""
        
        viewModelScope.launch {
            try {
                repo.insert(
                    Music(
                        cateId = cateId,
                        title = title,
                        content = content
                    )
                )
            } catch (e: Exception) {
                
            }
        }
    }

    fun deleteById(musicId: Long) {
        viewModelScope.launch {
            try {
                
                repo.deleteById(musicId)
                
            } catch (ex: Exception) {
                
            }
        }
    }
}
