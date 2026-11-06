// Database Module
package cn.primeessence.garden

import android.content.Context
import androidx.room.Room

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): GardenData {
        return Room.databaseBuilder(
            context,
            GardenData::class.java,
            "garden_db"
        ).build()
    }

    @Provides
    fun provideNoteDao(db: GardenData): NoteDao = db.noteDao()

}