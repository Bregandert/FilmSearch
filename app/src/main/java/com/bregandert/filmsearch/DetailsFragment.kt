package com.bregandert.filmsearch

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bregandert.filmsearch.MainActivity.Companion.FILM
import com.bregandert.filmsearch.MainActivity.Companion.TRANSITION_NAME
import com.bregandert.filmsearch.MainActivity.Companion.POSITION
import com.bregandert.filmsearch.MainActivity.Companion.POSTER
import com.bregandert.filmsearch.MainActivity.Companion.DESCRIPTION
import com.bregandert.filmsearch.databinding.FragmentDetailsBinding
import com.google.android.material.snackbar.Snackbar


class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var film: Film


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(layoutInflater)

        film = (
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arguments?.getParcelable(FILM, Film::class.java)
                } else {
                    arguments?.getParcelable(FILM) as Film?
                }
                ) ?: return binding.root

        initDetails()
        setFavoriteIcon()
        setShareIcon()
        return binding.root
    }

    private fun initDetails() {
        //Устанавливаем заголовок
        binding.detailsToolbar.title = film.title
        //Устанавливаем постер
        binding.detailsPoster.setImageResource(film.poster)
        //Устанавливаем описание
        binding.detailsDescription.text = film.description
    }

    private fun setFavoriteIcon() {

        // changing 'add to favorites' fab icon depending on status
        binding.favoritesFab.setImageResource(
            if (film.isFavorite) R.drawable.ic_favorite
            else R.drawable.ic_favorite_border
        )
        // setting 'add to favorites' fab click listener
        binding.favoritesFab.setOnClickListener {
            if (!film.isFavorite) {
                binding.favoritesFab.setImageResource(R.drawable.ic_favorite)
                film.isFavorite = true
            } else {
                binding.favoritesFab.setImageResource(R.drawable.ic_favorite_border)
                film.isFavorite = false
            }
        }
    }

    private fun setShareIcon (){
        // setting share fab click listener
        binding.detailsFab.setOnClickListener {
            //Создаем интент
            val intent = Intent()
            //Указываем action с которым он запускается
            intent.action = Intent.ACTION_SEND
            //Кладем данные о нашем фильме
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Check out this film: ${film.title} \n\n ${film.description}"
            )
            //Указываем MIME тип, чтобы система знала, какое приложения предложить
            intent.type = "text/plain"
            //Запускаем наше активити
            startActivity(Intent.createChooser(intent, "Share To:"))
        }
    }
}