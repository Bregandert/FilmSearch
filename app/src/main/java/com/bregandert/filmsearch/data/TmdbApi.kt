package com.bregandert.filmsearch.data

import com.bregandert.filmsearch.data.entity.TmdbResults
import io.reactivex.rxjava3.core.Observable

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("3/movie/{category}")
    fun getFilms(
        @Path("category") category: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Observable<TmdbResults>

    @GET("3/search/movie")
    fun searchFilms(
        @Query("query") query: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Observable<TmdbResults>
}