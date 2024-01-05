package com.bregandert.filmsearch.domain

import androidx.lifecycle.LiveData
import com.bregandert.filmsearch.data.APIKey
import com.bregandert.filmsearch.data.entity.Film
import com.bregandert.filmsearch.data.entity.TmdbResults
import com.bregandert.filmsearch.data.MainRepository
import com.bregandert.filmsearch.data.PreferenceProvider
import com.bregandert.filmsearch.data.TmdbApi

import com.bregandert.filmsearch.utils.Converter

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// класс для взаимодействия с базой данных фильма, внешним API и настройками
class Interactor(
    private val repo: MainRepository,
    private val retrofitService: TmdbApi,
    private val preferences: PreferenceProvider
) {

    //В конструктор мы будем передавать коллбэк из вью модели, чтобы реагировать на то, когда фильмы будут получены
    //и страницу, которую нужно загрузить (это для пагинации)
    fun getFilmsFromApi(page: Int, callback: ApiCallback) {

        retrofitService.getFilms(
            getDefaultCategoryFromPreferences(),
            APIKey.KEY,
            "ru-RU",
            page).enqueue(object : Callback<TmdbResults> {
            override fun onResponse(
                call: Call<TmdbResults>,
                response: Response<TmdbResults>) {

                //При успехе мы вызываем метод передаем onSuccess и в этот коллбэк список фильмов
                val list = Converter.convertApiListToDtoList(response.body()?.tmdbFilms)
//                кладем фильмы в бд
                saveFilmsToDB(list)
                callback.onSuccess()
            }

            override fun onFailure(call: Call<TmdbResults>, t: Throwable) {
                //В случае провала вызываем другой метод коллбека
                callback.onFailure()
            }
        })
    }

//    Работаем с локальной db
    fun clearLocalFilmsDB() {
        repo.clearFilmsDB()
    }

    fun saveFilmsToDB(list: List<Film>) {
        repo.putFilmsToDb(list)
    }

    fun getFilmsFromDB(): LiveData<List<Film>> = repo.getAllFilmsFromDB()

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