package com.bregandert.filmsearch.domain

import com.bregandert.filmsearch.data.APIKey
import com.bregandert.filmsearch.data.Entity.TmdbResults
import com.bregandert.filmsearch.data.MainRepository
import com.bregandert.filmsearch.data.TmdbApi
//import com.bregandert.filmsearch.di.modules.FilmRepositoryInterface
import com.bregandert.filmsearch.utils.Converter
import com.bregandert.filmsearch.viewmodel.HomeFragmentViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Interactor(private val repo: MainRepository, private val retrofitService: TmdbApi) {

    //В конструктор мы будем передавать коллбэк из вью модели, чтобы реагировать на то, когда фильмы будут получены
    //и страницу, которую нужно загрузить (это для пагинации)
    fun getFilmsFromApi(page: Int, callback: HomeFragmentViewModel.ApiCallback) {
        retrofitService.getFilms(APIKey.KEY, "ru-RU", page).enqueue(object : Callback<TmdbResults> {


            override fun onResponse(call: Call<TmdbResults>, response: Response<TmdbResults>) {
                //При успехе мы вызываем метод передаем onSuccess и в этот коллбэк список фильмов
                val list = Converter.convertApiListToDtoList(response.body()?.tmdbFilms)
//                кладем фильмы в бд
                list.forEach {
                    repo.putToDb(film = it)
                }
                callback.onSuccess(list)
            }

            override fun onFailure(call: Call<TmdbResults>, t: Throwable) {
                //В случае провала вызываем другой метод коллбека
                callback.onFailure()
            }
        })
    }

    fun getFilmsFromDB(): List<Film> = repo.getAllFromDB()
}