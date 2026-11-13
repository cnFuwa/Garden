// MateRepo
package cn.primeessence.garden

import javax.inject.Inject
import android.content.Context
import android.content.res.AssetManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class MateRepo @Inject constructor(
    private val dao: MateDao
) {
    suspend fun getLatest(): List<LiteMate> = dao.getLatest()
    suspend fun getTotal(): Long = dao.getTotal()
    suspend fun getCount(cateId: Long): Long = dao.getCount(cateId)
    suspend fun getIds(cateId: Long): List<Long> = dao.getIds(cateId)
    suspend fun getById(mateId: Long): Mate? = dao.getById(mateId)
    suspend fun insert(item: Mate): Long = dao.insert(item)
    suspend fun insertAll(items: List<Mate>) = dao.insertAll(items)
    suspend fun update(item: Mate): Int = dao.update(item)
    suspend fun deleteById(mateId: Long): Int = dao.deleteById(mateId)
    
    suspend fun loadAssetsFile(
         context: Context,
         fileName: String,
         title: String,
         cateId: Long
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
                    if (bufferList.size >= AppNumItem.BATCH_SIZE) {
                        val text: String = bufferList.joinToString("\n")
                        dao.insert(
                            Mate(
                                title = title,
                                cateId = cateId,
                                content = text,
                                enabled = 1
                            )
                        )
                        bufferList.clear()
                    }
                }
                if (bufferList.isNotEmpty()) {
                    val text2: String = bufferList.joinToString("\n")
                    dao.insert(
                        Mate(
                            title = title,
                            cateId = cateId,
                            content = text2,
                            enabled = 1
                        )
                    )
                    bufferList.clear()
                }
            }
        }
    }

}
