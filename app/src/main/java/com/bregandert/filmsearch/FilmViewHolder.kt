package com.bregandert.filmsearch

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bregandert.filmsearch.databinding.ActivityMainBinding
import com.bregandert.filmsearch.databinding.FilmItemBinding
import com.bumptech.glide.Glide
import java.text.ParsePosition


//В конструктор класс передается layout, который мы создали(film_item.xml)
class FilmViewHolder(val itemFilm: View) : RecyclerView.ViewHolder(itemFilm){

    private lateinit var binding : FilmItemBinding

    fun bindData(film: Film, clickListener: FilmListRecyclerAdapter.OnItemClickListener, position: Int) {



//      Используем Glide для постеров
//        Glide.with(binding.root)  //контейнер, наш список фильмов
//            .load(film.poster) //картинка для постера которую загружаем
//            .centerCrop()
//            .into(binding.poster) // закидываем эту картинку FilmItemBinding
//
//        binding.poster.transitionName = MainActivity.TRANSITION_NAME + position

        binding.poster.setImageResource(film.poster)

        binding.title.text = film.title
        binding.description.text = film.description
        binding.root.setOnClickListener {
            clickListener.click(film, position, binding)
        }
        bindingAdapterPosition

    }






}

