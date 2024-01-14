package com.bregandert.filmsearch.view.rv_viewholders


import androidx.recyclerview.widget.RecyclerView
import com.bregandert.filmsearch.App
import com.bregandert.filmsearch.data.ApiConstants


import com.bregandert.filmsearch.databinding.FilmItemBinding
import com.bregandert.filmsearch.data.entity.Film
import com.bregandert.filmsearch.view.rv_adapters.FilmListRecyclerAdapter
import com.bumptech.glide.Glide



//В конструктор класс передается layout, который мы создали(film_item.xml)
class FilmViewHolder(val binding: FilmItemBinding) : RecyclerView.ViewHolder(binding.root){


    fun bindData(film: Film, clickListener:FilmListRecyclerAdapter.OnItemClickListener, position: Int) {

//      Используем Glide для постеров
        Glide.with(binding.root)  //контейнер, наш список фильмов
            .load(ApiConstants.IMAGES_URL + "w342" + film.poster) //картинка для постера которую загружаем
            .centerCrop()
            .into(binding.poster) // закидываем эту картинку FilmItemBinding

        binding.poster.transitionName = App.instance.TRANSITION_NAME + position

        binding.title.text = film.title
        binding.description.text = film.description
        binding.root.setOnClickListener {
            clickListener.click(film, position, binding)
        }
        //Устанавливаем рейтинг
        binding.ratingView.setProgress((film.rating * 10).toInt())
        bindingAdapterPosition
    }


}

