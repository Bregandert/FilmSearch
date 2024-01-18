package com.bregandert.filmsearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

import com.bregandert.filmsearch.App
import com.bregandert.filmsearch.data.entity.Film
import com.bregandert.filmsearch.domain.Interactor
import com.bregandert.filmsearch.utils.SingleLiveEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeFragmentViewModel(state: SavedStateHandle): ViewModel() {

    @Inject
    lateinit var interactor: Interactor

    val filmsList: Flow<List<Film>>
    val apiErrorEvent = SingleLiveEvent<String>()
    private var toLoadFromApi = true
    private var page = 0

    val showProgressBar: Channel<Boolean>
    init {
        App.instance.dagger.inject(this)
        showProgressBar = interactor.progressBarState
        filmsList = interactor.getFilmsFromDB()
        loadFirstPage()
    }



    fun addNextPage() {
        if(!toLoadFromApi) return // грузим следующую страницу только если загружаем дынные из Api


        interactor.getFilmsFromApi(++page, object : Interactor.ApiCallback {
            override fun onSuccess() {
//                showProgressBar.postValue(false) //скрываем ProgressBar по завершении загрузки
            }
            override fun onFailure() {
                apiErrorEvent.postValue("MainFragment")
//                showProgressBar.postValue(false)
                page--
            }
        })
    }

    fun loadFirstPage() {
        val currentTime = System.currentTimeMillis()
        val savedTime = interactor.getLastAPIRequestTime()

        page = 0
        // если с момента последнего вызова API или изменения категории прошло больше установленного времени, запросите API еще раз
        if (
            (currentTime - savedTime) > App.instance.API_REQUEST_TIME_INTERVAL ||
            interactor.getDefaultCategoryFromPreferences() != interactor.getCategoryInDB()
        ) {
            toLoadFromApi = true
            interactor.clearLocalFilmsDB()
            interactor.saveCategoryInDB(interactor.getDefaultCategoryFromPreferences())
            interactor.saveLastAPIRequestTime()

            addNextPage()
        } else { // иначе грузим из db
            toLoadFromApi = false

        }
    }



}