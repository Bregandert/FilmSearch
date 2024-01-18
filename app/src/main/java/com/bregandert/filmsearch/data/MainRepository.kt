package com.bregandert.filmsearch.data

import androidx.lifecycle.LiveData
import com.bregandert.filmsearch.data.dao.FilmDao
import com.bregandert.filmsearch.data.entity.Film
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.util.concurrent.Executors

class MainRepository(private val filmDao: FilmDao) {

    fun putFilmsToDb(films: List<Film>?) {
//    Запросы в БД должны быть в отдельном потоке
//        Executors.newSingleThreadExecutor().execute {
            filmDao.insertAll(films)
//        }
    }
    fun getAllFilmsFromDB(): Flow<List<Film>> {
        return filmDao.getCashedFilms()
    }

    fun clearFilmsDB() {
        Executors.newSingleThreadExecutor().execute {
            filmDao.deleteAll()
        }
    }

    fun getFavouriteFilmsFromDB(): Flow<List<Film>> {
        return emptyFlow()
    }

}