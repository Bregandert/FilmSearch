package com.bregandert.filmsearch.domain

import androidx.lifecycle.LiveData
import com.bregandert.filmsearch.data.APIKey
import com.bregandert.filmsearch.data.entity.Film
import com.bregandert.filmsearch.data.entity.TmdbResults
import com.bregandert.filmsearch.data.MainRepository
import com.bregandert.filmsearch.data.PreferenceProvider
import com.bregandert.filmsearch.data.TmdbApi

import com.bregandert.filmsearch.utils.Converter
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// класс для взаимодействия с базой данных фильма, внешним API и настройками
class Interactor(
    private val repo: MainRepository,
    private val retrofitService: TmdbApi,
    private val preferences: PreferenceProvider
) {

    var progressBarState : BehaviorSubject<Boolean> = BehaviorSubject.create()
    val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)


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
        repo.putFilmsToDb(list)
    }

    fun getFilmsFromDB(): Observable<List<Film>> = repo.getAllFilmsFromDB()

//    fun getFavouriteFilmsFromDB(): Flow<List<Film>> = repo.getFavouriteFilmsFromDB()

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

//    interface ApiCallback {
//        fun onSuccess()
//        fun onFailure()
//    }
}