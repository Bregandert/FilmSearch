package com.bregandert.filmsearch.domain

import com.bregandert.filmsearch.data.APIKey
import com.bregandert.filmsearch.data.entity.Film
import com.bregandert.filmsearch.data.MainRepository
import com.bregandert.filmsearch.data.PreferenceProvider

import com.bregandert.filmsearch.utils.Converter
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

// класс для взаимодействия с базой данных фильма, внешним API и настройками
class Interactor(
    private val repo: MainRepository,
    private val retrofitService: com.bregandert.retrofit.TmdbApi,
    private val preferences: PreferenceProvider
) {

    var progressBarState : BehaviorSubject<Boolean> = BehaviorSubject.create()
    private lateinit var favoritesTmbdId: List<Int>

    init {
        getFavouriteFilmFromDB()
            .subscribeOn(Schedulers.io())
            .map {
                val ids: MutableList<Int> = mutableListOf<Int>()
                it.forEach { film -> ids.add(film.tmdbId) }
                ids
            }
            .subscribe {
                favoritesTmbdId = it
            }
    }



    fun getFilmsFromApi(page: Int) {

//        показываем ProgressBar
        progressBarState.onNext(true)

//        Переключаемся на RxJava observable
        retrofitService.getFilms(
            getDefaultCategoryFromPreferences(),
            APIKey.KEY,
            "ru-RU",
            page
        )
            .map { dto ->
                Converter.convertApiListToDtoList(dto.tmdbFilms)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe { list ->
                saveFilmsToDB(list)
                progressBarState.onNext(false)
            }
    }

    fun searchFilmsFromApi(query: String, page: Int){
//        показываем ProgressBar
        progressBarState.onNext(true)

//        ищем фильм по строке из Api
        retrofitService.searchFilms(
            query,
            APIKey.KEY,
            "ru-Ru",
            page
        )
            .doOnError { t -> null }
            .map {dto ->
                Converter.convertApiListToDtoList(dto.tmdbFilms)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe { list ->
                saveFilmsToDB(list)
                progressBarState.onNext(false)
            }
    }


//    Работаем с локальной db
    fun clearLocalFilmsDB() {
        repo.clearFilmsDB()
    }

    fun clearListAfterCategoryChange() {
        Completable.fromSingle<List<Film>> {
            repo.clearFilmsDB()
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun saveFilmsToDB(list: List<Film>) {
        list.forEach{
            it.isFavorite = favoritesTmbdId.contains(it.tmdbId)
        }
        repo.saveFilmsToDb(list)
    }

    fun getFilmsFromDB(): Observable<List<Film>> = repo.getAllFilmsFromDB()

    //    Работаем с локально базой данных избранных фильмов
    fun getFavouriteFilmFromDB(): Observable<List<Film>> = repo.getFavoriteFilmsFromDB()

    fun saveFilmToFavorites(film: Film) {
        repo.saveFilmToFavorites(film)
    }

    fun deleteFilmFromFavorites(film: Film) {
        repo.deleteFilmFromFavorites(film)
    }

//    Взаимодействуем с дефолтным значением категории
    fun saveDefaultCategoryToPreferences(category: String) {
        preferences.saveDefaultCategory(category)
    }

    fun getDefaultCategoryFromPreferences() = preferences.getDefaultCategory()

//    Время последнего обращения к внешней API
    fun saveLastAPIRequestTime() {
        preferences.saveLastAPIRequestTime()
    }

    fun getLastAPIRequestTime() = preferences.getLastAPIRequestTime()

//    Сохранение категории в локальной db
    fun saveCategoryInDB(category: String) {
        preferences.saveCategoryInDB(category)
    }

    fun getCategoryInDB() = preferences.getCategoryInDB()

    fun isFavorite(film: Film): Observable<Boolean> {
        return repo.isFilmInFavorites(film)
    }


}