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
    suspend fun getLatest(): List<Mate> = dao.getLatest()
    suspend fun getTotal(): Long = dao.getTotal()
    suspend fun getCount(cateId: Long): Long = dao.getCount(cateId)
    suspend fun getIds(cateId: Long): List<Long> = dao.getIds(cateId)
    suspend fun getById(mateId: Long): Mate? = dao.getById(mateId)
    suspend fun insert(item: Mate): Long = dao.insert(item)
    suspend fun insertAll(items: List<Mate>) = dao.insertAll(items)
    suspend fun update(item: Mate): Int = dao.update(item)
    suspend fun deleteById(mateId: Long): Int = dao.deleteById(mateId)
    
}
