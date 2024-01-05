package com.bregandert.filmsearch.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bregandert.filmsearch.data.dao.FilmDao
import com.bregandert.filmsearch.data.entity.Film

@Database(entities = [Film::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
}