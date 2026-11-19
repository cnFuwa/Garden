// FilterError ViewModel
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
class FilterErrorViewModel @Inject constructor(
    private val repo: MetaRepo
) : ViewModel() {

    private val _meta = MutableStateFlow<Meta?>(null)
    val result: StateFlow<Meta?> = _meta.asStateFlow()
    
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
    
    fun filterError(content: String): String {
        val rep = "e: file:///home/runner/work/Garden/Garden/app/src/main/kotlin/cn/primeessence/garden/"
        return content.trim()
            .split("\n")
            .map { it.trim() }
            .filter { it.startsWith("e:") }
            .map { it.replace(rep, "") }
            .joinToString("\n")
    }


    fun submit(
        title: String,
        content: String,
        enabled: Int,
        isDeleted: Int
    ) {
        viewModelScope.launch {
            try {
                val newContent = filterError(content)
                _meta.update { old ->
                    old?.copy(
                        title = title,
                        content = newContent,
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