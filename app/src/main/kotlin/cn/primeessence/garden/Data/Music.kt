// Music
package cn.primeessence.garden

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music")
data class Music(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val cateId: Long = 0L,
    val title: String = "",
    val content: String = "",
    val enabled: Int = 0,
    val isDeleted: Int = 0
)

data class LiteMusic(
    val id: Long = 0L,
    val cateId: Long = 0L,
    val title: String = "",
    val enabled: Int = 0
)