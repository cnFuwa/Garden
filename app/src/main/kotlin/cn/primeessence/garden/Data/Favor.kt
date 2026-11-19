// Favor
package cn.primeessence.garden

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favor")
data class Favor(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val cateId: Long = 0L,
    val title: String = "",
    val startUrl: String = "",
    val content: String = "",
    val enabled: Int = 0,
    val isDeleted: Int = 0
)

data class LiteFavor(
    val id: Long = 0L,
    val cateId: Long = 0L,
    val title: String = "",
    val enabled: Int = 0
)