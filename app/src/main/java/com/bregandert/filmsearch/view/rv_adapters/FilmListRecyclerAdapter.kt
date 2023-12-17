package com.bregandert.filmsearch.view.rv_adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bregandert.filmsearch.view.rv_viewholders.FilmViewHolder
import com.bregandert.filmsearch.databinding.FilmItemBinding
import com.bregandert.filmsearch.domain.Film


//в параметр передаем слушатель, чтобы мы потом могли обрабатывать нажатия из класса Activity
class FilmListRecyclerAdapter(private val clickListener: OnItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){



    //Здесь у нас хранится список элементов для RV
    val films = mutableListOf<Film>()

    //Этот метод нужно переопределить на возврат количества элементов в списке RV
    override fun getItemCount() = films.size

    //В этом методе мы привязываем наш ViewHolder и передаем туда "надутую" верстку нашего фильма
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FilmViewHolder(FilmItemBinding.inflate(LayoutInflater.from(parent.context), parent,false))

    }



    //инициализируем данные из холдера для каждого фильма
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        проверяем какой у нас ViewHolder
        when (holder) {
            is FilmViewHolder -> {
                val film = films[position]
                holder.bindData(film, clickListener, position)
            }
        }
    }

    //Инициализируем базу данных адаптера с данным списком фильмов. Используем DiffUtils дл изменений
    fun addItems(newList: List<Film>) {
        val numbersDiff = FilmDiff(films, newList)
        val diffResult = DiffUtil.calculateDiff(numbersDiff)
        films.clear()
        films.addAll(newList)
        diffResult.dispatchUpdatesTo(this)

    }


    //Интерфейс для обработки кликов
    interface OnItemClickListener {
        fun click(film: Film, position: Int, binding: FilmItemBinding)
    }
}