package com.bregandert.filmsearch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bregandert.filmsearch.databinding.ActivityMainBinding
import com.bregandert.filmsearch.databinding.FilmItemBinding
import java.text.ParsePosition

//import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var backPressed = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigation()
        initFilmsDB()


//        Запускаем Homefragment
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_placeholder, HomeFragment())
            .addToBackStack(null)
            .commit()

    }

    private fun initFilmsDB() {
        filmsDataBase = mutableListOf<Film>(
            Film(1,"Elemental", R.drawable.elemental, "Elemental - fantasy animation film."),
            Film(2, "Barbie", R.drawable.barbie, "Margo Robbi is in the comedy about the famous doll."),
            Film(3, "Indiana Jones and the dial of destiny", R.drawable.indiana_jones_and_the_dial_of_destiny, "New part of famous franshise."),
            Film(4, "Oppenheimer", R.drawable.oppenheimer, "Film is about creator of the atomic bomb."),
            Film(5,"Insidious the red door", R.drawable.insidious_the_red_door, "New part of one of the scare horror."),
            Film(6, "Mission impossible dead reckoning peart one", R.drawable.mission_impossible__dead_reckoning_part_one, "Special agent Itan Hant safe the World again."),
            Film(7, "No hard fillings", R.drawable.no_hard_feelings, "Light comedy with beautiful Jenifer Lawrence"),
            Film(8,"Spiderman across the spiderverse", R.drawable.spiderman_across_the_spiderverse, "Continuation of animation film"),
            Film(9, "Transformers rise of the beasts", R.drawable.transformers_rise_of_the_beasts, "Beasts in the World of big robots")
        )
    }



    // Check back pressed. If 2 times in less than 2 sec om main screen - exit. If not main screen (fragment) - back.
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount <= 1) {
            if (backPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                onBackPressedDispatcher.onBackPressed()
                finish()
            } else {
                Toast.makeText(this, getString(R.string.double_back_toast), Toast.LENGTH_SHORT).show()
            }

            backPressed = System.currentTimeMillis()
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    // Contains constant - interval for double tap in ms

    private fun initNavigation() {

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    Toast.makeText(this, "Настройки", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }

        binding.navyAppBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.favorites -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_placeholder, FavoritesFragment())
                        .addToBackStack(null)
                        .commit()
                    true
                }
                R.id.watch_later -> {
                    Toast.makeText(this, "Смотреть позже", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.selections -> {
                    Toast.makeText(this, "Подборки", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    fun launchDetailsFragment(film: Film, position: Int, filmItemBinding: FilmItemBinding) {
        val bundle = Bundle()
        bundle.putParcelable(FILM, film)
        bundle.putInt(POSITION, position)
        val fragment = DetailsFragment()
        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        const val TIME_INTERVAL = 2000L
        const val FILM = "film"
        const val POSITION = "position"
        const val TRANSITION_NAME = "transition"
        var filmsDataBase = mutableListOf<Film>()
        val favoriteFilms = mutableListOf<Film>()
    }


}


