package com.bregandert.filmsearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bregandert.filmsearch.App
import com.bregandert.filmsearch.domain.Film
import com.bregandert.filmsearch.domain.Interactor
import javax.inject.Inject

class HomeFragmentViewModel: ViewModel() {
    val filmsListLiveData = MutableLiveData<List<Film>>()

    @Inject
    lateinit var interactor: Interactor
    private var page = 0
    init {
        App.instance.dagger.inject(this)
        loadFirstPage()
    }

    interface ApiCallback {
        fun onSuccess(films: List<Film>)
        fun onFailure()
    }

    fun addNextPage() {
        interactor.getFilmsFromApi(++page, object : ApiCallback {
            override fun onSuccess(films: List<Film>) {
                filmsListLiveData.value = filmsListLiveData.value?.plus(films) ?: films
            }
            override fun onFailure() {
                filmsListLiveData.postValue(interactor.getFilmsFromDB())
                page--
            }
        })
    }

    fun loadFirstPage() {
        filmsListLiveData.value = emptyList()
        page = 0
        addNextPage()
    }

}