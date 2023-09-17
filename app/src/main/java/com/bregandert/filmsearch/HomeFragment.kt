package com.bregandert.filmsearch


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bregandert.filmsearch.databinding.FragmentHomeBinding
import com.bregandert.filmsearch.MainActivity.Companion.filmsDataBase
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.appcompat.widget.SearchView.OnQueryTextListener



class HomeFragment : Fragment() {

    private lateinit var filmsAdapter: FilmListRecyclerAdapter
    private lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Поиск не при нажатии на иконку, а на все поле поиска
        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
        }

        //Подключаем слушателя изменений введенного текста в поиска
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            //Этот метод отрабатывает при нажатии кнопки "поиск" на софт клавиатуре
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            //Этот метод отрабатывает на каждое изменения текста
            override fun onQueryTextChange(newText: String?): Boolean {
                //Если ввод пуст то вставляем в адаптер всю БД
                if (newText.isNullOrBlank()) {
                    filmsAdapter.addItems(filmsDataBase)
                return true
            }
            //Фильтруем список на поиск подходящих сочетаний
            val result = filmsDataBase.filter {
                //Чтобы все работало правильно, нужно запрос и имя фильма приводить к нижнему регистру
                it.title.lowercase()
                    .contains(newText.lowercase())
            }
            //Добавляем в адаптер
            filmsAdapter.addItems(result as MutableList<Film>)
            return true
            }

        })

        // Прячем поле для поиска при скролле вниз и показываем при скролле наверх
        binding.mainRecycler.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) {
                    binding.searchView.visibility = View.GONE
                } else if (dy > 0) {
                    binding.searchView.visibility = View.VISIBLE
                }
            }
        })

        initHomeFragment()
    }

    //находим наш RV
    private fun initHomeFragment() {

            filmsAdapter = FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                    override fun click(film: Film) {
                        (requireActivity() as MainActivity).launchDetailsFragment(film)
                    }
                })

            filmsAdapter.addItems(filmsDataBase)
            //Присваиваем адаптер
            binding.mainRecycler.adapter = filmsAdapter

            //Применяем декоратор для отступов
            val decorator = TopSpacingItemDecoration(8)
            binding.mainRecycler.addItemDecoration(decorator)

        }

}