// Note
package cn.primeessence.garden

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val cateId: Long = 0L,
    val title: String = "",
    val content: String = "",
    val enabled: Int = 0,
    val isDeleted: Int = 0
)