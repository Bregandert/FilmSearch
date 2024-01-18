package com.bregandert.filmsearch.domain

import androidx.lifecycle.LiveData
import com.bregandert.filmsearch.data.APIKey
import com.bregandert.filmsearch.data.entity.Film
import com.bregandert.filmsearch.data.entity.TmdbResults
import com.bregandert.filmsearch.data.MainRepository
import com.bregandert.filmsearch.data.PreferenceProvider
import com.bregandert.filmsearch.data.TmdbApi

import com.bregandert.filmsearch.utils.Converter
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

    var progressBarState = Channel<Boolean>(Channel.CONFLATED)
    val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)


    fun getFilmsFromApi(page: Int, callback: ApiCallback) {

//        показываем ProgressBar
        scope.launch {
            progressBarState.send(true)
        }

        retrofitService.getFilms(
            getDefaultCategoryFromPreferences(),
            APIKey.KEY,
            "ru-RU",
            page
        ).enqueue(object : Callback<TmdbResults> {
            override fun onResponse(
                call: Call<TmdbResults>,
                response: Response<TmdbResults>
            ) {

                scope.launch {
//                    Преобразование ответа API в список фильмов с помощью потока
                    val list = response.body()?.tmdbFilms?.asFlow()?.map {
                        Converter.convertApiToFilm(it)
                    }?.toList()
                    saveFilmsToDB(list)
                    progressBarState.send(false)
                }
                callback.onSuccess()
            }


            override fun onFailure(call: Call<TmdbResults>, t: Throwable) {
                //В случае провала вызываем другой метод коллбека
                scope.launch {
                    progressBarState.send(false)
                }
                callback.onFailure()
            }
        })
    }


//    Работаем с локальной db
    fun clearLocalFilmsDB() {
        repo.clearFilmsDB()
    }

    fun saveFilmsToDB(list: List<Film>?) {
        repo.putFilmsToDb(list)
    }

    fun getFilmsFromDB(): Flow<List<Film>> = repo.getAllFilmsFromDB()

    fun getFavouriteFilmsFromDB(): Flow<List<Film>> = repo.getFavouriteFilmsFromDB()

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

    interface ApiCallback {
        fun onSuccess()
        fun onFailure()
    }
}