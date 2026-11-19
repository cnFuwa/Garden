// Favor Dao
package cn.primeessence.garden

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.OnConflictStrategy

@Dao
interface FavorDao {
    @Query("SELECT id,cateId,title,enabled FROM favor ORDER BY id ASC")
    suspend fun getLatest(): List<LiteFavor>

    @Query("SELECT COUNT(*) FROM favor")
    suspend fun getTotal(): Long

    @Query("SELECT COUNT(*) FROM favor WHERE cateId = :cateId")
    suspend fun getCount(cateId: Long): Long

    @Query("SELECT * FROM favor WHERE id = :favorId")
    suspend fun getById(favorId: Long): Favor?
    
    @Query("SELECT id FROM favor WHERE cateId = :cateId")
    suspend fun getIds(cateId: Long): List<Long>

    @Insert
    suspend fun insert(item: Favor): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Favor>)

    @Update
    suspend fun update(item: Favor): Int

    @Query("UPDATE favor SET isDeleted = 1 WHERE id = :favorId")
    suspend fun deleteById(favorId: Long): Int

}