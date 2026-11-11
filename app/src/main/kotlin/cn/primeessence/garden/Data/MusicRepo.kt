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
    suspend fun getLatest(): List<Music> = dao.getLatest()
    suspend fun getTotal(): Long = dao.getTotal()
    suspend fun getCount(musicId: Long): Long = dao.getCount(cateId)
    suspend fun getIds(cateId: Long): List<Long> = dao.getIds(cateId)
    suspend fun getById(musicId: Long): Music? = dao.getById(musicId)
    suspend fun insert(item: Music): Long = dao.insert(item)
    suspend fun insertAll(items: List<Music>) = dao.insertAll(items)
    suspend fun update(item: Music): Int = dao.update(item)
    suspend fun deleteById(musicId: Long): Int = dao.deleteById(musicId)
}
