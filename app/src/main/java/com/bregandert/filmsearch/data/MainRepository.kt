package com.bregandert.filmsearch.data

import android.content.ContentValues
import android.database.Cursor
import com.bregandert.filmsearch.data.db.DatabaseHelper
import com.bregandert.filmsearch.domain.Film

class MainRepository(databaseHelper: DatabaseHelper) {

//    Инициализируем объект для взаимодействия с БД
    private val sqlDb = databaseHelper.readableDatabase
//    Создаем курсор для обработки запросов из БД
    private lateinit var cursor: Cursor

    fun putToDb (film: Film) {
//        Создаем объект, который будет хранить пары ключ-значение,
//        для того чтобы класть нужные данные в таблицу
        val cv = ContentValues()
        cv.apply {
            put(DatabaseHelper.COLUMN_TITLE, film.title)
            put(DatabaseHelper.COLUMN_POSTER, film.poster)
            put(DatabaseHelper.COLUMN_DESCRIPTION, film.description)
            put(DatabaseHelper.COLUMN_RATING, film.rating)
        }
//        кладем фильм в БД
        sqlDb.insert(DatabaseHelper.TABLE_NAME, null, cv)
    }

    fun getAllFromDB(): List<Film> {
//        Создаем курсор на основании запроса Получить все из таблицы
        cursor = sqlDb.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_NAME}", null)
//        сюда будем сохранять результат полученных по запросу данных
        val result = mutableListOf<Film>()
//        Проверяем есть ли хоть одна строка в ответе на запрос
        if (cursor.moveToFirst()) {
//            итерируемся по таблице пока есть записи и создаем на основании объект Film
            do {
                val title = cursor.getString(1)
                val poster = cursor.getString(2)
                val description = cursor.getString(3)
                val rating = cursor.getDouble(4)

                result.add(Film(title, poster, description, rating))
            } while (cursor.moveToNext())
        }
//        Возвращаем список фильмов
        return result

    }

    // Редактирование ДБ
    fun updateFilmInDB(film: Film) {
        val cv = ContentValues()
        cv.apply {
            put(DatabaseHelper.COLUMN_POSTER, film.poster)
            put(DatabaseHelper.COLUMN_DESCRIPTION, film.description)
            put(DatabaseHelper.COLUMN_RATING, film.rating)
        }

        sqlDb.update(
            DatabaseHelper.TABLE_NAME,
            cv,
            DatabaseHelper.COLUMN_TITLE + " = ?",
            arrayOf(film.title)
        )
    }

    // Удаление из ДБ
    fun deleteFilmFromDB(film: Film) {
        sqlDb.delete(
            DatabaseHelper.TABLE_NAME,
            DatabaseHelper.COLUMN_TITLE + " = ?",
            arrayOf(film.title)
        )
    }

    // Выбор по рейтингу
    fun getFilmsFromDBWithRating(minRating: Double): List<Film> {
        cursor = sqlDb.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_NAME} " +
                    "WHERE ${DatabaseHelper.COLUMN_RATING} >= ?",
            arrayOf(minRating.toString())
        )

        val result = mutableListOf<Film>()
        cursor.use { c ->
            if (c.moveToFirst()) {
                do {
                    val title = c.getString(1)
                    val poster = c.getString(2)
                    val description = c.getString(3)
                    val rating = c.getDouble(4)

                    result.add(Film(title, poster, description, rating))
                } while (c.moveToNext())
            }
        }
        return result
    }

    // выбор по названию
    fun getFilmsFromDBByTitle(titlePattern: String): List<Film> {
        cursor = sqlDb.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_NAME} " +
                    "WHERE ${DatabaseHelper.COLUMN_TITLE} LIKE ?",
            arrayOf("%$titlePattern%")
        )

        val result = mutableListOf<Film>()
        cursor.use { c ->
            if (c.moveToFirst()) {
                do {
                    val title = c.getString(1)
                    val poster = c.getString(2)
                    val description = c.getString(3)
                    val rating = c.getDouble(4)

                    result.add(Film(title, poster, description, rating))
                } while (c.moveToNext())
            }
        }
        return result
    }

    // выбор по описанию
    fun getFilmsFromDBByDescription(descriptionPattern: String): List<Film> {
        cursor = sqlDb.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_NAME} " +
                    "WHERE ${DatabaseHelper.COLUMN_DESCRIPTION} LIKE ?",
            arrayOf("%$descriptionPattern%")
        )

        val result = mutableListOf<Film>()
        cursor.use { c ->
            if (c.moveToFirst()) {
                do {
                    val title = c.getString(1)
                    val poster = c.getString(2)
                    val description = c.getString(3)
                    val rating = c.getDouble(4)

                    result.add(Film(title, poster, description, rating))
                } while (c.moveToNext())
            }
        }
        return result
    }

}