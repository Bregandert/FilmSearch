package com.bregandert.filmsearch.viewmodel


import androidx.lifecycle.ViewModel
import com.bregandert.filmsearch.App
import com.bregandert.filmsearch.domain.Interactor

import javax.inject.Inject

class FavoriteFragmentViewModel: ViewModel() {

    @Inject
    lateinit var interactor: Interactor

    init {

        App.instance.dagger.inject(this)
//        interactor.getFilmsFromApi(1, object : Interactor.ApiCallback {
//            override fun onSuccess() {
//
//            }
//            override fun onFailure() {
//            }
//        })
//        filmsList = interactor.getFavouriteFilmsFromDB()
    }

}