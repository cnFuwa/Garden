// NoteDao
package cn.primeessence.garden

import androidx.room.Dao
//import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.OnConflictStrategy

@Dao
interface NoteDao {
    @Query("SELECT * FROM note ORDER BY id ASC")
    suspend fun getLatest(): List<Note>

    @Query("SELECT COUNT(*) FROM note")
    suspend fun getTotal(): Long

    @Query("SELECT COUNT(*) FROM note WHERE cateId = :cateId")
    suspend fun getCount(cateId: Long): Long

    @Query("SELECT * FROM note WHERE id = :noteId")
    suspend fun getById(noteId: Long): Note?
    
    @Query("SELECT id FROM note WHERE cateId = :cateId")
    suspend fun getIds(cateId: Long): List<Long>

    @Insert
    suspend fun insert(item: Note): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Note>)

    @Update
    suspend fun update(item: Note): Int

    @Query("UPDATE note SET isDeleted = 1 WHERE id = :noteId")
    suspend fun deleteById(noteId: Long): Int

}