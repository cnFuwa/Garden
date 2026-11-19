// Garden Data

package cn.primeessence.garden

import androidx.room.Database
import androidx.room.RoomDatabase
import android.content.Context

@Database(
    entities = [
        Favor::class,
        Note::class,
        Mate::class,
        Music::class,
        Meta::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GardenData : RoomDatabase() {
    abstract fun favorDao(): FavorDao
    abstract fun noteDao(): NoteDao
    abstract fun mateDao(): MateDao
    abstract fun musicDao(): MusicDao
    abstract fun metaDao(): MetaDao
}
