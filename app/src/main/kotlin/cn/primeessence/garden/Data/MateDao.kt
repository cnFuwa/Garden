// MateDao
package cn.primeessence.garden

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.OnConflictStrategy

@Dao
interface MateDao {
    @Query("SELECT id,cateId,title,enabled FROM mate ORDER BY id ASC")
    suspend fun getLatest(): List<LiteMate>

    @Query("SELECT COUNT(*) FROM mate")
    suspend fun getTotal(): Long

    @Query("SELECT COUNT(*) FROM mate WHERE cateId = :cateId")
    suspend fun getCount(cateId: Long): Long

    @Query("SELECT * FROM mate WHERE id = :mateId")
    suspend fun getById(mateId: Long): Mate?
    
    @Query("SELECT id FROM mate WHERE cateId = :cateId")
    suspend fun getIds(cateId: Long): List<Long>

    @Insert
    suspend fun insert(item: Mate): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Mate>)

    @Update
    suspend fun update(item: Mate): Int

    @Query("UPDATE mate SET isDeleted = 1 WHERE id = :mateId")
    suspend fun deleteById(mateId: Long): Int

}