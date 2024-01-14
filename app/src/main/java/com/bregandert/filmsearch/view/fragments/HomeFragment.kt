package com.bregandert.filmsearch.view.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bregandert.filmsearch.databinding.FragmentHomeBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bregandert.filmsearch.R
import com.bregandert.filmsearch.view.rv_adapters.FilmListRecyclerAdapter
import com.bregandert.filmsearch.view.MainActivity
import com.bregandert.filmsearch.view.rv_adapters.TopSpacingItemDecoration
import com.bregandert.filmsearch.databinding.FilmItemBinding
import com.bregandert.filmsearch.data.entity.Film
import com.bregandert.filmsearch.utils.AnimationHelper
import com.bregandert.filmsearch.viewmodel.HomeFragmentViewModel

class HomeFragment : Fragment() {

    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var filmsAdapter: FilmListRecyclerAdapter

    private val viewModel: HomeFragmentViewModel by activityViewModels()

    //    Создадим переменную, куда будем класть нашу БД из ViewModel, чтобы у нас не сломался поиск
    private var filmsDataBase = listOf<Film>()
        //Используем backing field
        set(value) {
            //Если придет такое же значение, то мы выходим из метода
            if (field == value) return
            //Если пришло другое значение, то кладем его в переменную
            field = value
            //Обновляем RV адаптер
            filmsAdapter.addItems(field)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        homeBinding = FragmentHomeBinding.inflate(layoutInflater)
        return homeBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        readFilmsDBFromViewModel()

        //Поиск не при нажатии на иконку, а на все поле поиска
        homeBinding.searchView.setOnClickListener {
            homeBinding.searchView.isIconified = false
        }

        initSearchView()
//        находим наш RV
        initHomeFragment()

        initPullToRefresh()
        refreshFragment()


        AnimationHelper.performFragmentCircularRevealAnimation(
            homeBinding.homeFragmentRoot,
            requireActivity(),
            0
        )


    }

    private fun readFilmsDBFromViewModel() {
        viewModel.filmsListLiveData.observe(viewLifecycleOwner) {
            filmsDataBase = it
        }

        viewModel.showProgressBar.observe(viewLifecycleOwner) {
            homeBinding.progressBar.isVisible = it
        }

        viewModel.apiErrorEvent.observe(viewLifecycleOwner) {
            Toast.makeText(homeBinding.root.context, it + ":" + getString(R.string.api_error_message), Toast.LENGTH_LONG).show()
        }


    }

    private fun initSearchView() {

        //Подключаем слушателя изменений введенного текста в поиска
        homeBinding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
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
                filmsAdapter.addItems(result)
                return true
            }

        })
    }


    //находим наш RV
    private fun initHomeFragment() {

        filmsAdapter =
            FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                //            если кликнули фильм то запускаем фрагмент с этим фильмом
                override fun click(film: Film, position: Int, binding: FilmItemBinding) {
                    (requireActivity() as MainActivity).launchDetailsFragment(
                        film,
                        position,
                        binding
                    )
                }
            })

//        добавляем все фильмы в ресайклер вью адаптер
        filmsAdapter.addItems(filmsDataBase)
        homeBinding.mainRecycler.adapter = filmsAdapter

//        добавляем отступы декоратором
        val decorator = TopSpacingItemDecoration(8)
        homeBinding.mainRecycler.addItemDecoration(decorator)

//        Скрываем/показываем поисковую панель в зависимости от направления скрола
//        Загружаем новые данные когда мы в конце листа
        val scrollListener = object : OnScrollListener() {

            private val layoutManager: LinearLayoutManager =
                homeBinding.mainRecycler.layoutManager as LinearLayoutManager
            var isLoading = false

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) {
                    homeBinding.searchView.visibility = View.GONE
                } else if (dy > 0) {
                    homeBinding.searchView.visibility = View.VISIBLE
                }
                val visibleItemCount: Int = layoutManager.childCount
                val totalItemCount: Int = layoutManager.itemCount
                val firstVisibleItems = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading) {
                    if (visibleItemCount + firstVisibleItems >= totalItemCount - 3) {
                        isLoading = true
                        viewModel.addNextPage()
                        isLoading = false
                    }
                }

            }
        }

        homeBinding.mainRecycler.addOnScrollListener(scrollListener)

    }

    private fun initPullToRefresh() {
        homeBinding.pullToRefresh.setOnRefreshListener {
            refreshFragment()
            homeBinding.pullToRefresh.isRefreshing = false
        }
    }

    private fun refreshFragment() {
        viewModel.loadFirstPage()
        readFilmsDBFromViewModel()
        filmsAdapter.addItems(filmsDataBase)
    }
}

