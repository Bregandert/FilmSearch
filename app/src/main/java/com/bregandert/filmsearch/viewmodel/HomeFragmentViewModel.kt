package com.bregandert.filmsearch.viewmodel


import androidx.lifecycle.ViewModel

import com.bregandert.filmsearch.App
import com.bregandert.filmsearch.data.entity.Film
import com.bregandert.filmsearch.domain.Interactor
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

import javax.inject.Inject

class HomeFragmentViewModel: ViewModel() {

    @Inject
    lateinit var interactor: Interactor

    val filmsList: Observable<List<Film>>
//    val apiErrorEvent = SingleLiveEvent<String>()
    private var toLoadFromApi = true
    private var page = 0

    val showProgressBar: BehaviorSubject<Boolean>
    init {
        App.instance.dagger.inject(this)
        showProgressBar = interactor.progressBarState
        filmsList = interactor.getFilmsFromDB()
        loadFirstPage(false)
    }



    fun addNextPage() {
        if(!toLoadFromApi) return // грузим следующую страницу только если загружаем дынные из Api
        interactor.getFilmsFromApi(++page)
    }

    fun loadFirstPage(fromApi: Boolean) {
        val currentTime = System.currentTimeMillis()
        val savedTime = interactor.getLastAPIRequestTime()

        page = 0
        // если с момента последнего вызова API или изменения категории прошло больше установленного времени, запросите API еще раз
        if (fromApi ||
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

    fun loadSearchResults(query: String) {
        page = 0
        interactor.clearLocalFilmsDB()
        addSearchResultsPage(query)
    }

    fun addSearchResultsPage(query: String) {
        interactor.searchFilmsFromApi(query, ++page)
    }


}