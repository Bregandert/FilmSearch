package com.bregandert.filmsearch.utils

import com.bregandert.filmsearch.data.entity.TmdbFilm
import com.bregandert.filmsearch.data.entity.Film

object Converter {

    fun convertApiListToDtoList(list: List<TmdbFilm>?): List<Film> {
        val result = mutableListOf<Film>()
        list?.forEach {
            try {
            result.add(convertApiToFilm(it))
        } catch (e: Exception) {

        }
    }
        return result
    }

    fun convertApiToFilm(apiData: TmdbFilm): Film {
        return Film(
            title = apiData.title,
            poster = apiData.posterPath,
            description = apiData.overview,
            rating = apiData.voteAverage / 10f,
            isFavorite = false
        )
    }
}