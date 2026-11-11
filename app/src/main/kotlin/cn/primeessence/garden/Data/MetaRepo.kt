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
    suspend fun getLatest(): List<Meta> = dao.getLatest()
    suspend fun getTotal(): Long = dao.getTotal()
    suspend fun getById(metaId: Long): Meta? = dao.getById(metaId)
    suspend fun insert(item: Meta): Long = dao.insert(item)
    suspend fun insertAll(items: List<Meta>) = dao.insertAll(items)
    suspend fun update(item: Meta): Int = dao.update(item)
    suspend fun deleteById(metaId: Long): Int = dao.deleteById(metaId)
}
