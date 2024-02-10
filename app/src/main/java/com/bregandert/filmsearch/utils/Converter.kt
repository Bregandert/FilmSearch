package com.bregandert.filmsearch.utils

import com.bregandert.filmsearch.data.entity.Film
import com.bregandert.retrofit.entity.TmdbFilm

object Converter {

    fun convertApiListToDtoList(list: List<com.bregandert.retrofit.entity.TmdbFilm>?): List<Film> {
        val result = mutableListOf<Film>()
        list?.forEach {
            try {
            result.add(convertApiToFilm(it))
        } catch (e: Exception) {

        }
    }
        return result
    }

    fun convertApiToFilm(apiData: com.bregandert.retrofit.entity.TmdbFilm): Film {
        return Film(
            title = apiData.title,
            poster = apiData.posterPath,
            description = apiData.overview,
            rating = apiData.voteAverage / 10f,

            isFavorite = false
        )
    }
}