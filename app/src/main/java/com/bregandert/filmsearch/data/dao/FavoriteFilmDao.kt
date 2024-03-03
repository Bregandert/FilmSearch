package com.bregandert.filmsearch.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bregandert.filmsearch.data.entity.FavoriteFilm
import io.reactivex.rxjava3.core.Observable

@Dao
interface FavoriteFilmDao {
    @Query("SELECT * FROM favorite_films")
    fun getFavoriteFilms(): Observable<List<FavoriteFilm>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(film: FavoriteFilm)

    @Query("DELETE FROM favorite_films")
    fun deleteAll()

    @Delete
    fun delete(film: FavoriteFilm)

    @Query("DELETE FROM favorite_films WHERE tmdb_id = :tmdbId")
    fun deleteByTmdbId(tmdbId: Int)

    @Query("SELECT EXISTS (SELECT * FROM favorite_films WHERE tmdb_id = :tmdbId)")
    fun existsByTmdbId(tmdbId: Int): Observable<Boolean>

}