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
import java.text.ParsePosition

//import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var filmsAdapter: FilmListRecyclerAdapter

    val filmsDataBase = mutableListOf<Film>(
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initNavigation()


            //находим наш RV
            binding.mainRecycler.apply {

                //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
                //оставим его пока пустым, он нам понадобится во второй части задания
                filmsAdapter =
                    FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                        override fun click(film: Film) {
                            //Создаем бандл и кладем туда объект с данными фильма
                            val bundle = Bundle()
                            //Первым параметром указывается ключ, по которому потом будем искать, вторым сам
                            //передаваемый объект
                            bundle.putParcelable("film", film)

                            //Запускаем наше активити
                            val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                            //Прикрепляем бандл к интенту
                            intent.putExtras(bundle)
                            //Запускаем активити через интент
                            startActivity(intent)

                        }
                    })

                //Присваиваем адаптер
                adapter = filmsAdapter
                //Присваиваем layoutmanager
                layoutManager = LinearLayoutManager(this@MainActivity)
                //Применяем декоратор для отступов
                val decorator = TopSpacingItemDecoration(8)
                addItemDecoration(decorator)


//                fun updatedata(newList: ArrayList<Film>){
//                    val oldList = adapter.
//
//                    val filmDiff = FilmDiff(oldList, newList)
//                    val diffResult = DiffUtil.calculateDiff(filmDiff)
//
//                    adapter = newList.add(Intent)
//                    adapter = filmsAdapter
//
//                }

            }
        //Кладем нашу БД в RV
        filmsAdapter.addItems(filmsDataBase)

    }

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
                    Toast.makeText(this, "Избранное", Toast.LENGTH_SHORT).show()
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
}


