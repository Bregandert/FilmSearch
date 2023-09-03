package com.bregandert.filmsearch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.bregandert.filmsearch.databinding.FragmentDetailsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar


class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        initDetails()
        return binding.root
    }

    private fun initDetails(){
        val film = arguments?.get("film") as Film

        //Устанавливаем заголовок

        binding.detailsToolbar.title = film.title
        binding.detailsPoster.setImageResource(film.poster)
        binding.detailsDescription.text = film.description

        //Обрабатываем логику при нажатии кнопок навигации
        binding.bottomNavy.setOnItemSelectedListener{

            when (it.itemId) {
                R.id.favorites_act -> Snackbar.make(binding.mainActivity, getString(R.string.activity_favorites), Snackbar.LENGTH_LONG).show()

                R.id.watch_later_act -> Snackbar.make(binding.mainActivity, getString(R.string.activity_watch_later), Snackbar.LENGTH_LONG).show()
            }
            false
        }
    }
}