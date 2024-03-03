package com.bregandert.filmsearch.data

import com.bregandert.filmsearch.data.dao.FavoriteFilmDao
import com.bregandert.filmsearch.data.dao.FilmDao
import com.bregandert.filmsearch.data.entity.Film
import com.bregandert.filmsearch.utils.Converter
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.Executors

//Отвечает за обмен данными между ViewModels и БД
class MainRepository(
    private val filmDao: FilmDao,
    private val favoriteFilmDao: FavoriteFilmDao
) {

    fun saveFilmsToDb(films: List<Film>) {
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

    fun saveFilmToFavorites(film: Film) {
        Executors.newSingleThreadExecutor().execute() {
            favoriteFilmDao.insert(Converter.filmToFavorite(film))
        }
    }

    fun deleteFilmFromFavorites(film: Film) {
        Executors.newSingleThreadExecutor().execute{
            favoriteFilmDao.deleteByTmdbId(film.tmdbId)
        }
    }

    fun getFavoriteFilmsFromDB(): Observable<List<Film>> {
        return favoriteFilmDao.getFavoriteFilms().map {
            Converter.favoriteListToFilmList(it)
        }
    }

    fun isFilmInFavorites(film: Film): Observable<Boolean> {
        return favoriteFilmDao.existsByTmdbId(film.tmdbId)
    }



}