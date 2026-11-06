// NoteRepo
package cn.primeessence.garden

//import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import android.content.Context
import android.content.res.AssetManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class NoteRepo @Inject constructor(
    private val dao: NoteDao
) {
    val BATCH_SIZE = 3000

    suspend fun getLatest(): List<Note> = dao.getLatest()
    suspend fun getTotal(): Long = dao.getTotal()
    suspend fun getById(noteId: Long): Note? = dao.getById(noteId)
    suspend fun insert(item: Note): Long = dao.insert(item)
    suspend fun insertAll(items: List<Note>) = dao.insertAll(items)
    suspend fun update(item: Note): Int = dao.update(item)
    suspend fun deleteById(noteId: Long): Int = dao.deleteById(noteId)
    
    suspend fun importAssetsFile(
         context: Context, fileName: String
    ) = withContext(Dispatchers.IO) {
        val assetManager: AssetManager = context.assets
        assetManager.open(fileName).use { inputStream ->
            BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8)).use { reader ->
                val bufferList = mutableListOf<Sring>()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    if (line.isNullOrBlank()) continue
                    bufferList.add(line.trim())
                    if (bufferList.size >= BATCH_SIZE) {
                        val text: String = bufferList.joinToString("\n")
                        dao.insert(Note(content = text))
                        bufferList.clear()
                    }
                }
                if (bufferList.isNotEmpty()) {
                        val text2: String = bufferList.joinToString("\n")
                        dao.insert(Note(content = text2))
                        bufferList.clear()
                }
            }
        }
    }
}
