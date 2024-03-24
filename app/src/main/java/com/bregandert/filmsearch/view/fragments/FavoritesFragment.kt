package com.bregandert.filmsearch.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

import com.bregandert.filmsearch.view.rv_adapters.FilmListRecyclerAdapter
import com.bregandert.filmsearch.view.MainActivity

import com.bregandert.filmsearch.databinding.FragmentFavoritesBinding
import com.bregandert.filmsearch.databinding.FilmItemBinding
import com.bregandert.filmsearch.view.rv_adapters.TopSpacingItemDecoration
import com.bregandert.filmsearch.data.entity.Film
import com.bregandert.filmsearch.utils.AnimationHelper
import com.bregandert.filmsearch.utils.AutoDisposable
import com.bregandert.filmsearch.utils.addTo
import com.bregandert.filmsearch.viewmodel.FavoriteFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers


class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var filmsAdapter: FilmListRecyclerAdapter
    private val viewModel: FavoriteFragmentViewModel by viewModels<FavoriteFragmentViewModel>()
    private val autoDisposable = AutoDisposable()

    private var filmsDataBase = listOf<Film>()
        set(value) {
            if (field == value) return
            field = value
            filmsAdapter.addItems(field)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        autoDisposable.bindTo(lifecycle)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.filmsList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list -> filmsDataBase = list }
            .addTo(autoDisposable)


        initFavorites()
        AnimationHelper.performFragmentCircularRevealAnimation(binding.root, requireActivity(), 1)
    }

    private fun initFavorites(){

        filmsAdapter = FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
            override fun click(film : Film, position: Int, binding: FilmItemBinding) {
                (requireActivity() as MainActivity).launchDetailsFragment(film)
            }
        })
        filmsAdapter.addItems(filmsDataBase.filter { film -> film.isFavorite  })
        binding.favoritesRecycler.adapter = filmsAdapter
        val decorator = TopSpacingItemDecoration(8)
        binding.favoritesRecycler.addItemDecoration(decorator)

    }

}