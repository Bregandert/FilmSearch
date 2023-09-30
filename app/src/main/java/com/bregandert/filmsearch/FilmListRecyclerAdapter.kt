package com.bregandert.filmsearch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bregandert.filmsearch.databinding.FilmItemBinding
import com.bumptech.glide.Glide

//в параметр передаем слушатель, чтобы мы потом могли обрабатывать нажатия из класса Activity
class FilmListRecyclerAdapter(val context: Context, films: MutableList<Film>) :
RecyclerView.Adapter<FilmListRecyclerAdapter.FilmViewHolder>(){

    //Здесь у нас хранится список элементов для RV
    val films = mutableListOf<Film>()

    //Этот метод нужно переопределить на возврат количества элементов в списке RV
    override fun getItemCount() = films.size

    //В этом методе мы привязываем наш ViewHolder и передаем туда "надутую" верстку нашего фильма
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
//        return FilmViewHolder(FilmItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        return FilmViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.film_item, parent, false))
    }

//    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
//        TODO("Not yet implemented")
//    }

    //инициализируем данные из холдера для каждого фильма
    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        when (holder) {
            is FilmViewHolder -> {
                val film = films[position]
//                holder.bindData(film, clickListener, position)

            }
        }
    }

    //Инициализируем базу данных адаптера с данным списком фильмов. Используем DiffUtils дл изменений
    fun addItems(newList: MutableList<Film>) {


        val numbersDiff = FilmDiff(films, newList)
        val diffResult = DiffUtil.calculateDiff(numbersDiff)
        films.clear()
        films.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
//
    }


    //Интерфейс для обработки кликов
    interface OnItemClickListener {
        fun click(film: Film, position: Int, binding: FilmItemBinding)
    }

    inner class FilmViewHolder(val itemFilm: View) : RecyclerView.ViewHolder(itemFilm){

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
}