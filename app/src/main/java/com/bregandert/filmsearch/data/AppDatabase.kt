package com.bregandert.filmsearch.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bregandert.filmsearch.data.dao.FavoriteFilmDao
import com.bregandert.filmsearch.data.dao.FilmDao
import com.bregandert.filmsearch.data.entity.FavoriteFilm
import com.bregandert.filmsearch.data.entity.Film

@Database(entities = [Film::class, FavoriteFilm::class], version = 2, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
    abstract fun favoriteFilmsDao(): FavoriteFilmDao
}