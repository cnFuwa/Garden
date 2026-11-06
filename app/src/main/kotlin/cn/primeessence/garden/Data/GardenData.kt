// Garden Data

package cn.primeessence.garden

import androidx.room.Database
import androidx.room.RoomDatabase
import android.content.Context

@Database(
    entities = [
        Note::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class GardenData : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
