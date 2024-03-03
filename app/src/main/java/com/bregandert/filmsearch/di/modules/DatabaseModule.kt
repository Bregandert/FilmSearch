package com.bregandert.filmsearch.di.modules

import android.content.Context
import androidx.room.Room
import com.bregandert.filmsearch.data.MainRepository
import com.bregandert.filmsearch.data.dao.FilmDao
import com.bregandert.filmsearch.data.AppDatabase
import com.bregandert.filmsearch.data.dao.FavoriteFilmDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "film_db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideFilmDao (db: AppDatabase):FilmDao = db.filmDao()

    @Singleton
    @Provides
    fun provideFavoriteFilmDao(db: AppDatabase): FavoriteFilmDao = db.favoriteFilmsDao()
    @Provides
    @Singleton
    fun provideRepository(filmDao: FilmDao, favoriteFilmDao: FavoriteFilmDao) = MainRepository(filmDao, favoriteFilmDao)



}

