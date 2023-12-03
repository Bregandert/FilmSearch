package com.bregandert.filmsearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bregandert.filmsearch.App
import com.bregandert.filmsearch.domain.Film
import com.bregandert.filmsearch.domain.Interactor

class HomeFragmentViewModel: ViewModel() {
    val filmsListLiveData = MutableLiveData<List<Film>>()
    private var interactor: Interactor = App.instance.interactor
    private var page = 0
    init {
        addNextPage()
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
                page--
            }
        })
    }

}