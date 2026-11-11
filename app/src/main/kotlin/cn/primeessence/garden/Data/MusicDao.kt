// MusicDao
package cn.primeessence.garden

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.OnConflictStrategy

@Dao
interface MusicDao {
    @Query("SELECT id,cateId,title FROM music ORDER BY id DESC")
    suspend fun getLatest(): List<LiteMusic>

    @Query("SELECT COUNT(*) FROM music")
    suspend fun getTotal(): Long

    @Query("SELECT COUNT(*) FROM music WHERE cateId = :cateId")
    suspend fun getCount(cateId: Long): Long

    @Query("SELECT * FROM music WHERE id = :musicId")
    suspend fun getById(musicId: Long): Music?
    
    @Query("SELECT id FROM music WHERE cateId = :cateId")
    suspend fun getIds(cateId: Long): List<Long>

    @Insert
    suspend fun insert(item: Music): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Music>)

    @Update
    suspend fun update(item: Music): Int

    @Query("UPDATE music SET isDeleted = 1 WHERE id = :musicId")
    suspend fun deleteById(musicId: Long): Int

}