package com.bregandert.filmsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bregandert.filmsearch.databinding.FilmItemBinding

//в параметр передаем слушатель, чтобы мы потом могли обрабатывать нажатия из класса Activity
class FilmListRecyclerAdapter(private var clickListener : OnItemClickListener) :
RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    //Здесь у нас хранится список элементов для RV
    private val items = mutableListOf<Film>()

    //Этот метод нужно переопределить на возврат количества элементов в списке RV
    override fun getItemCount() = items.size

    //В этом методе мы привязываем наш ViewHolder и передаем туда "надутую" верстку нашего фильма
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FilmViewHolder(FilmItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    //В этом методе будет привязка полей из объекта Film к View из film_item.xml
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FilmViewHolder -> {
                val film = items[position]
                holder.binding.poster.setImageResource(items[position].poster)
                holder.binding.title.text = items[position].title
                holder.binding.description.text = items[position].description
                //Обрабатываем нажатие на весь элемент целиком(можно сделать на отдельный элемент
                //например, картинку) и вызываем метод нашего листенера, который мы получаем из
                //конструктора адаптера
                holder.binding.itemContainer.setOnClickListener {
                    clickListener.click(film)
                }

            }
        }
    }

    //Метод для добавления объектов в наш список
    fun addItems(newList: MutableList<Film>) {


        val numbersDiff = FilmDiff(items, newList)
        val diffResult = DiffUtil.calculateDiff(numbersDiff)
        items.clear()
        items.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
//
    }


    //Интерфейс для обработки кликов
    interface OnItemClickListener {
        fun click(film: Film)
    }



}