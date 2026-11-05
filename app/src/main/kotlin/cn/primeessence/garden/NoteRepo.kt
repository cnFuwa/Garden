// NoteRepo
package cn.primeessence.garden

//import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class NoteRepo @Inject constructor(
    private val dao: NoteDao
) {
    suspend fun getLatest(): List<Note> = dao.getLatest()
    suspend fun getTotal(): Long = dao.getTotal()
    suspend fun getById(noteId: Long): Note? = dao.getById(noteId)
    suspend fun insert(item: Note): Long = dao.insert(item)
    suspend fun insertAll(items: List<Note>) = dao.insertAll(items)
    suspend fun update(item: Note): Int = dao.update(item)
    suspend fun deleteById(noteId: Long): Int = dao.deleteById(noteId)
}
