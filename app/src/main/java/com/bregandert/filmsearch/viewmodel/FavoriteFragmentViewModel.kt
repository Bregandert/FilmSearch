package com.bregandert.filmsearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bregandert.filmsearch.App
import com.bregandert.filmsearch.domain.Film
import com.bregandert.filmsearch.domain.Interactor
import javax.inject.Inject

class FavoriteFragmentViewModel: ViewModel() {

    val filmsListLiveData = MutableLiveData<List<Film>>()

    @Inject
    lateinit var interactor: Interactor

    init {

        App.instance.dagger.inject(this)
        interactor.getFilmsFromApi(1, object : HomeFragmentViewModel.ApiCallback {
            override fun onSuccess(films: List<Film>) {
                filmsListLiveData.postValue(films)
            }
            override fun onFailure() {
            }
        })
    }

}