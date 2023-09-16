package com.bregandert.filmsearch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bregandert.filmsearch.databinding.FragmentDetailsBinding
import com.bregandert.filmsearch.databinding.FragmentFavoritesBinding
import com.google.android.material.snackbar.Snackbar
import com.bregandert.filmsearch.MainActivity.Companion.filmsDataBase
import com.bregandert.filmsearch.MainActivity.Companion.favoriteFilms


class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var filmsAdapter: FilmListRecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(layoutInflater)
        initFavorites()
        return binding.root
    }

    private fun initFavorites(){
        favoriteFilms.clear()
        for(film in filmsDataBase) {
            if (film.isFavorite) favoriteFilms.add(film)

        }
        filmsAdapter = FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
            override fun click(film : Film) {
                (requireActivity() as MainActivity).launchDetailsFragment(film)
            }
        })
        filmsAdapter.addItems(favoriteFilms)
        binding.favoritesRecycler.adapter = filmsAdapter
        val decorator =TopSpacingItemDecoration(8)
        binding.favoritesRecycler.addItemDecoration(decorator)

    }

}