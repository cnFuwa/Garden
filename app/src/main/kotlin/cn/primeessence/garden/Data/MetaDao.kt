// MetaDao
package cn.primeessence.garden

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.OnConflictStrategy

@Dao
interface MetaDao {
    @Query("SELECT id,title,content,enabled FROM meta ORDER BY id ASC")
    suspend fun getLatest(): List<LiteMeta>

    @Query("SELECT COUNT(*) FROM meta")
    suspend fun getTotal(): Long

    @Query("SELECT * FROM meta WHERE id = :metaId")
    suspend fun getById(metaId: Long): Meta?
    
    @Insert
    suspend fun insert(item: Meta): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Meta>)

    @Update
    suspend fun update(item: Meta): Int

    @Query("UPDATE meta SET isDeleted = 1 WHERE id = :metaId")
    suspend fun deleteById(metaId: Long): Int

}