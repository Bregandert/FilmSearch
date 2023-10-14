package com.bregandert.filmsearch


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bregandert.filmsearch.databinding.ActivityMainBinding
import com.bregandert.filmsearch.databinding.FilmItemBinding




class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var backPressed = 0L

    companion object {
        const val TIME_INTERVAL = 2000L
        const val FILM = "film"
        const val POSITION = "position"
        const val TRANSITION_NAME = "transition"
        const val POSTER = "poster"
        const val DESCRIPTION = "description"
        const val FRAGMENT_TAG = "tag"
        var filmsDataBase = mutableListOf<Film>()
        val favoriteFilms = mutableListOf<Film>()
        val detailsFragment = DetailsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        Инициализируем слушатели меню навигации
        initNavigation()
//        инициализируем нашу ДБ
        initFilmsDB()


//        Запускаем Homefragment
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_placeholder, HomeFragment())
            .addToBackStack(FRAGMENT_TAG)
            .commit()

    }

    private fun initFilmsDB() {
        filmsDataBase = mutableListOf<Film>(
            Film(1,"Elemental", R.drawable.elemental, "Elemental - fantasy animation film.", 7.7f),
            Film(2, "Barbie", R.drawable.barbie, "Margo Robbi is in the comedy about the famous doll.", 4.5f),
            Film(3, "Indiana Jones and the dial of destiny", R.drawable.indiana_jones_and_the_dial_of_destiny, "New part of famous franshise.", 2.3f),
            Film(4, "Oppenheimer", R.drawable.oppenheimer, "Film is about creator of the atomic bomb.", 5.5f),
            Film(5,"Insidious the red door", R.drawable.insidious_the_red_door, "New part of one of the scare horror.", 3.3f),
            Film(6, "Mission impossible dead reckoning peart one", R.drawable.mission_impossible__dead_reckoning_part_one, "Special agent Itan Hant safe the World again.", 5.7f),
            Film(7, "No hard fillings", R.drawable.no_hard_feelings, "Light comedy with beautiful Jenifer Lawrence", 6.7f),
            Film(8,"Spiderman across the spiderverse", R.drawable.spiderman_across_the_spiderverse, "Continuation of animation film", 8.8f),
            Film(9, "Transformers rise of the beasts", R.drawable.transformers_rise_of_the_beasts, "Beasts in the World of big robots",6.5f)
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
                R.id.home -> {
                    val tag = "home"
                    val fragment = checkFragmentExistence(tag)
                    //В первом параметре, если фрагмент не найден и метод вернул null, то с помощью
                    //элвиса мы вызываем создание нвого фрагмента
                    changeFragment(fragment?:HomeFragment(), tag)
                    true
                }
                R.id.favorites -> {
                    val tag = "favorites"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment(fragment?: FavoritesFragment(), tag)
                    true
                }
                R.id.watch_later -> {
                    val tag = "watch_later"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment(fragment?: WatchLaterFragment(), tag)
                    true
                }
                R.id.selections -> {
                    val tag = "selections"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment(fragment?: SelectionsFragment(), tag)
                    true
                }
                else -> false

            }
        }
    }

    //Ищем фрагмент по тэгу, если он есть то возвращаем его, если нет - то null
    private fun checkFragmentExistence(tag: String): Fragment? = supportFragmentManager.findFragmentByTag(tag)

    private fun changeFragment(fragment: Fragment, tag: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment, tag)
            .addToBackStack(FRAGMENT_TAG)
            .commit()
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
            .addToBackStack(FRAGMENT_TAG)
            .commit()
    }
}


