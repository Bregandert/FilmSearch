package com.bregandert.filmsearch.utils

import com.bregandert.filmsearch.data.Entity.TmdbFilm
import com.bregandert.filmsearch.domain.Film

object Converter {

    fun convertApiListToDtoList(list: List<TmdbFilm>?): List<Film> {
        val result = mutableListOf<Film>()
        list?.forEach {
            result.add(
                Film(
                title = it.title,
                poster = it.posterPath,
                description = it.overview,
                rating = it.voteAverage,
                isFavorite = false
            )
            )
        }
        return result
    }
}