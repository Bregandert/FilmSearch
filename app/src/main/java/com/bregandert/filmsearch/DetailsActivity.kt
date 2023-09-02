package com.bregandert.filmsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.graphics.alpha
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.bregandert.filmsearch.R
import com.bregandert.filmsearch.databinding.ActivityDetailsBinding


class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)




        //Получаем наш фильм из переданного бандла
        val film = intent.extras?.get("film") as Film

        //Устанавливаем заголовок
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.details_toolbar).title = film.title
        //Устанавливаем картинку
        findViewById<AppCompatImageView>(R.id.details_poster).setImageResource(film.poster)
        //Устанавливаем описание
        findViewById<TextView>(R.id.details_description).text = film.description

        //Обрабатываем логику при нажатии кнопок навигации
        findViewById<BottomNavigationView>(R.id.bottom_navy).setOnItemSelectedListener{
            when (it.itemId) {
                R.id.favorites_act -> Snackbar.make(findViewById(R.id.main_activity), getString(R.string.activity_favorites), Snackbar.LENGTH_LONG).show()

                R.id.watch_later_act -> Snackbar.make(findViewById(R.id.main_activity), getString(R.string.activity_watch_later), Snackbar.LENGTH_LONG).show()
            }
            false


        }


    }
}


