package com.bregandert.filmsearch

import android.app.Application
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.bregandert.filmsearch.data.ApiConstants
import com.bregandert.filmsearch.data.MainRepository
import com.bregandert.filmsearch.data.TmdbApi
import com.bregandert.filmsearch.domain.Interactor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class App : Application() {
    lateinit var repo: MainRepository
    lateinit var interactor: Interactor
    lateinit var retrofitService: TmdbApi


        val TIME_INTERVAL = 2000L
        val FILM = "film"
        val POSITION = "position"
        val TRANSITION_NAME = "transition"
        val POSTER = "poster"
        val DESCRIPTION = "description"
        val FRAGMENT_TAG = "tag"



    override fun onCreate() {
        super.onCreate()
        //Инициализируем экземпляр App, через который будем получать доступ к остальным переменным
        instance = this
        //Инициализируем репозиторий
        repo = MainRepository()
        //Создаем кастомный клиент
        val okHttpClient = OkHttpClient.Builder()
            //Настриваем таймауты для медленного интрнета
            .callTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            //Добавляем логгер
            .addInterceptor(HttpLoggingInterceptor().apply {


                    level = HttpLoggingInterceptor.Level.BASIC

            })
            .build()
        //Создаем ретрофит
        val retrofit = Retrofit.Builder()
            //Указываем базовый URL из констант
            .baseUrl(ApiConstants.BASE_URL)
            //Добавляем конвертер
            .addConverterFactory(GsonConverterFactory.create())
            //Добавляем кастомный клиент
            .client(okHttpClient)
            .build()
        //Создаем сам сервис с методами для запросов
        retrofitService = retrofit.create(TmdbApi::class.java)
        //Инициализируем интерактор
        interactor = Interactor(repo, retrofitService)
    }

    companion object {
        //Здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
            //Приватный сеттер, чтобы нельзя было в эту переменную присвоить что-либо другое
            private set
    }

}