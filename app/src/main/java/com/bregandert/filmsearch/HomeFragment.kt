package com.bregandert.filmsearch


import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bregandert.filmsearch.databinding.FragmentHomeBinding
import com.bregandert.filmsearch.MainActivity.Companion.filmsDataBase
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.appcompat.widget.SearchView
import androidx.transition.Scene
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.bregandert.filmsearch.databinding.FilmItemBinding
import com.bregandert.filmsearch.databinding.MergeHomeScreenContentBinding
import com.bregandert.filmsearch.databinding.MergeHomeScreenContentBinding.bind


class HomeFragment : Fragment() {

    private lateinit var filmsAdapter: FilmListRecyclerAdapter
//    private lateinit var binding: FragmentHomeBinding

    private var _bindingHome: FragmentHomeBinding? = null
    private val bindingHome: FragmentHomeBinding get() = _bindingHome!!

    private var _bindingMerge: MergeHomeScreenContentBinding? = null
    private val bindingMerge: MergeHomeScreenContentBinding get() = _bindingMerge!!
//    private lateinit var _bindingMerge : MergeHomeScreenContentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return inflater.inflate(R.layout.fragment_home, container, false)
        _bindingHome = FragmentHomeBinding.inflate(inflater,container, false)
        _bindingMerge = bind(bindingHome.root)




        return bindingHome.root

//        binding = FragmentHomeBinding.inflate(layoutInflater)
//        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val scene = Scene.getSceneForLayout(_bindingHome?.homeFragmentRoot as ViewGroup, R.layout.merge_home_screen_content, requireContext())

        val searchSlide = Slide(Gravity.TOP).addTarget(_bindingMerge?.searchView as View) // почему null

        val resyclerSlide = Slide(Gravity.BOTTOM).addTarget(_bindingMerge?.mainRecycler!!)

        val customTransition = TransitionSet().apply {
            duration = 500
            addTransition(resyclerSlide)
            addTransition(searchSlide)

        }
        TransitionManager.go(scene, customTransition)

        //Поиск не при нажатии на иконку, а на все поле поиска
        _bindingMerge?.searchView?.setOnClickListener {
            _bindingMerge?.searchView?.isIconified = false
        }

        //Подключаем слушателя изменений введенного текста в поиска
        _bindingMerge?.searchView?.setOnQueryTextListener(object : OnQueryTextListener {
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
        _bindingMerge?.mainRecycler?.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) {
                    _bindingMerge?.searchView?.visibility = View.GONE
                } else if (dy > 0) {
                    _bindingMerge?.searchView?.visibility = View.VISIBLE
                }
            }
        })

        initHomeFragment()
    }

    //находим наш RV
    private fun initHomeFragment() {

            filmsAdapter = FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                    override fun click(film: Film, position: Int, binding: FilmItemBinding) {
                        (requireActivity() as MainActivity).launchDetailsFragment(film, position, binding)
                    }
                })

            filmsAdapter.addItems(filmsDataBase)
            //Присваиваем адаптер
            _bindingMerge?.mainRecycler?.adapter = filmsAdapter

            //Применяем декоратор для отступов
            val decorator = TopSpacingItemDecoration(8)
            _bindingMerge?.mainRecycler?.addItemDecoration(decorator)

        }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingHome = null
        _bindingMerge = null

    }

}