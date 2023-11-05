package com.bregandert.filmsearch.domain

import com.bregandert.filmsearch.data.MainRepository

class Interactor(val repo: MainRepository) {



    fun getFilmsDB(): List<Film> = repo.filmsDataBase
}