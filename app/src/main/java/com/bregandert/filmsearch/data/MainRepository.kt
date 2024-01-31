package com.bregandert.filmsearch.data

import androidx.lifecycle.LiveData
import com.bregandert.filmsearch.data.dao.FilmDao
import com.bregandert.filmsearch.data.entity.Film
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.util.concurrent.Executors

//Отвечает за обмен данными между ViewModels и БД
class MainRepository(private val filmDao: FilmDao) {

    fun putFilmsToDb(films: List<Film>) {
            filmDao.insertAll(films)
    }

    fun getAllFilmsFromDB(): Observable<List<Film>> {
        return filmDao.getCashedFilms()
    }

    fun clearFilmsDB() {
        Executors.newSingleThreadExecutor().execute {
            filmDao.deleteAll()
        }
    }

//    fun getFavouriteFilmsFromDB(): Flow<List<Film>> {
//        return emptyFlow()
//    }

}