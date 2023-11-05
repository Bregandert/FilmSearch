package com.bregandert.filmsearch

import android.app.Application
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.bregandert.filmsearch.data.MainRepository
import com.bregandert.filmsearch.domain.Interactor

class App : Application() {
    lateinit var repo: MainRepository
    lateinit var interactor: Interactor


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
        //Инициализируем интерактор
        interactor = Interactor(repo)
    }

    companion object {
        //Здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
            //Приватный сеттер, чтобы нельзя было в эту переменную присвоить что-либо другое
            private set
    }

}