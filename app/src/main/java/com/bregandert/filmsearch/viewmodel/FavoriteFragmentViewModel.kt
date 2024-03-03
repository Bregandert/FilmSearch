package com.bregandert.filmsearch.viewmodel


import androidx.lifecycle.ViewModel
import com.bregandert.filmsearch.App
import com.bregandert.filmsearch.data.entity.Film
import com.bregandert.filmsearch.domain.Interactor
import io.reactivex.rxjava3.core.Observable

import javax.inject.Inject

class FavoriteFragmentViewModel: ViewModel() {

    @Inject
    lateinit var interactor: Interactor
    val filmsList: Observable<List<Film>>

    init {

        App.instance.dagger.inject(this)
        filmsList = interactor.getFavouriteFilmFromDB()


    }

}