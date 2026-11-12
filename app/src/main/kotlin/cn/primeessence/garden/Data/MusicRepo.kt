// MusicRepo
package cn.primeessence.garden

import javax.inject.Inject
import android.content.Context
import android.content.res.AssetManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class MusicRepo @Inject constructor(
    private val dao: MusicDao
) {
    suspend fun getLatest(): List<LiteMusic> = dao.getLatest()
    suspend fun getTotal(): Long = dao.getTotal()
    suspend fun getCount(cateId: Long): Long = dao.getCount(cateId)
    suspend fun getIds(cateId: Long): List<Long> = dao.getIds(cateId)
    suspend fun getById(musicId: Long): Music? = dao.getById(musicId)
    suspend fun insert(item: Music): Long = dao.insert(item)
    suspend fun insertAll(items: List<Music>) = dao.insertAll(items)
    suspend fun update(item: Music): Int = dao.update(item)
    suspend fun deleteById(musicId: Long): Int = dao.deleteById(musicId)
    
    suspend fun loadAssetsFile(
         context: Context, fileName: String, cateId: Long
    ) = withContext(Dispatchers.IO) {
        val assetManager: AssetManager = context.assets
        assetManager.open(fileName).use { inputStream ->
            BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8)).use { reader ->
                val bufferList = mutableListOf<String>()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    if (line.isNullOrBlank()) continue
                    val rep = line.trim()
                    bufferList.add(rep)
                    if (bufferList.size >= BATCH_SIZE) {
                        val text: String = bufferList.joinToString("\n")
                        dao.insert(
                            Music(
                                title = fileName,
                                cateId = cateId,
                                content = text
                            )
                        )
                        bufferList.clear()
                    }
                }
                if (bufferList.isNotEmpty()) {
                        val text2: String = bufferList.joinToString("\n")
                        dao.insert(
                            Music(
                                title = fileName,
                                cateId = cateId,
                                content = text2
                            )
                        )
                        bufferList.clear()
                }
            }
        }
    }
}
