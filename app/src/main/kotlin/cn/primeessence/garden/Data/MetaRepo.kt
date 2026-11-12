// MetaRepo
package cn.primeessence.garden

import javax.inject.Inject
import android.content.Context
import android.content.res.AssetManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class MetaRepo @Inject constructor(
    private val dao: MetaDao
) {
    suspend fun getLatest(): List<LiteMeta> = dao.getLatest()
    suspend fun getTotal(): Long = dao.getTotal()
    suspend fun getById(metaId: Long): Meta? = dao.getById(metaId)
    suspend fun insert(item: Meta): Long = dao.insert(item)
    suspend fun insertAll(items: List<Meta>) = dao.insertAll(items)
    suspend fun update(item: Meta): Int = dao.update(item)
    suspend fun deleteById(metaId: Long): Int = dao.deleteById(metaId)
    
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
                            Meta(
                                title = fileName,
                                content = text
                            )
                        )
                        bufferList.clear()
                    }
                }
                if (bufferList.isNotEmpty()) {
                        val text2: String = bufferList.joinToString("\n")
                        dao.insert(
                            Meta(
                                title = fileName,
                                content = text2
                            )
                        )
                        bufferList.clear()
                }
            }
        }
    }

}
