package com.bregandert.filmsearch

import android.app.Application
import com.bregandert.filmsearch.di.AppComponent
import com.bregandert.filmsearch.di.DaggerAppComponent


class App : Application() {
    lateinit var dagger: AppComponent

        val TIME_INTERVAL = 2000L
        val FILM = "film"
        val POSITION = "position"
        val TRANSITION_NAME = "transition"
        val FRAGMENT_TAG = "tag"



    override fun onCreate() {
        super.onCreate()
        //Инициализируем экземпляр App, через который будем получать доступ к остальным переменным
        instance = this
        //Создаем компонент
        dagger = DaggerAppComponent.create()


    }

    companion object {
        //Здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
            //Приватный сеттер, чтобы нельзя было в эту переменную присвоить что-либо другое
            private set
    }

}