package com.bregandert.filmsearch.data

import androidx.lifecycle.LiveData
import com.bregandert.filmsearch.data.dao.FilmDao
import com.bregandert.filmsearch.data.entity.Film
import java.util.concurrent.Executors

class MainRepository(private val filmDao: FilmDao) {

    fun putFilmsToDb(films: List<Film>) {
//    Запросы в БД должны быть в отдельном потоке
        Executors.newSingleThreadExecutor().execute {
            filmDao.insertAll(films)
        }
    }
    fun getAllFilmsFromDB(): LiveData<List<Film>> {
        return filmDao.getCashedFilms()
    }

    fun clearFilmsDB() {
        Executors.newSingleThreadExecutor().execute {
            filmDao.deleteAll()
        }
    }

}