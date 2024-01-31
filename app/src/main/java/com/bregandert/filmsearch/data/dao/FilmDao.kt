package com.bregandert.filmsearch.data.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bregandert.filmsearch.data.entity.Film
import io.reactivex.rxjava3.core.Observable


//Помечаем, что это не просто интерфейс, в DAO - объект
@Dao
interface FilmDao {
//    Запрос на всю таблицу
    @Query("SELECT * FROM cashed_films")
    fun getCashedFilms(): Observable<List<Film>>

//    Кладем списком в БД, в случае конфликта перезаписываем
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Film>)

    @Query("DELETE FROM cashed_films")
    fun deleteAll()

}