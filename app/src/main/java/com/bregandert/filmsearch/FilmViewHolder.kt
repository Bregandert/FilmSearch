package com.bregandert.filmsearch


import androidx.recyclerview.widget.RecyclerView

import com.bregandert.filmsearch.databinding.FilmItemBinding
import com.bumptech.glide.Glide



//В конструктор класс передается layout, который мы создали(film_item.xml)
class FilmViewHolder(val binding: FilmItemBinding) : RecyclerView.ViewHolder(binding.root){


    fun bindData(film: Film, clickListener: FilmListRecyclerAdapter.OnItemClickListener, position: Int) {



//      Используем Glide для постеров
        Glide.with(binding.root)  //контейнер, наш список фильмов
            .load(film.poster) //картинка для постера которую загружаем
            .centerCrop()
            .into(binding.poster) // закидываем эту картинку FilmItemBinding

        binding.poster.transitionName = MainActivity.TRANSITION_NAME + position

//        binding.poster.setImageResource(film.poster)

        binding.title.text = film.title
        binding.description.text = film.description
        //Устанавливаем рейтинг
        binding.ratingDonut.setProgress((film.rating * 10).toInt())
        binding.root.setOnClickListener {
            clickListener.click(film, position, binding)
        }
//        adapterPosition
          bindingAdapterPosition
    }


}

