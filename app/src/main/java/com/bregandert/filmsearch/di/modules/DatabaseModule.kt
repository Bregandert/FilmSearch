package com.bregandert.filmsearch.di.modules

import android.content.Context
import androidx.room.Room
import com.bregandert.filmsearch.data.MainRepository
import com.bregandert.filmsearch.data.dao.FilmDao
import com.bregandert.filmsearch.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideFilmDao (context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "film_db"
        ).build().filmDao()

    @Provides
    @Singleton
    fun provideRepository(filmDao: FilmDao) = MainRepository(filmDao)



}

