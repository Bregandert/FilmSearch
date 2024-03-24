package com.bregandert.filmsearch

import android.app.Application
import com.bregandert.filmsearch.di.AppComponent
import com.bregandert.filmsearch.di.DaggerAppComponent
import com.bregandert.filmsearch.di.modules.DatabaseModule
import com.bregandert.filmsearch.di.modules.DomainModule
import com.bregandert.retrofit.DaggerRemoteComponent


class App : Application() {
    lateinit var dagger: AppComponent
    val FRAGMENT_TAG = "fragment"
    val BACK_CLICK_TIME_INTERVAL = 2000L
    val API_REQUEST_TIME_INTERVAL = 1000L * 60 * 10
    val FILM = "film"

    val TRANSITION_NAME = "transition"




    override fun onCreate() {
        super.onCreate()
        //Инициализируем экземпляр App, через который будем получать доступ к остальным переменным
        instance = this
        //Создаем компонент
        dagger = DaggerAppComponent.builder()
            .remoteProvider(DaggerRemoteComponent.create())
            .databaseModule(DatabaseModule())
            .domainModule(DomainModule(this))
            .build()
        }

    companion object {
        //Здесь статически хранится ссылка на экземпляр App
        lateinit var instance: App
            //Приватный сеттер, чтобы нельзя было в эту переменную присвоить что-либо другое
            private set
    }

}