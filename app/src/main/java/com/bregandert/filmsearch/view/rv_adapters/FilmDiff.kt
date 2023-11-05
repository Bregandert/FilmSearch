package com.bregandert.filmsearch.view.rv_adapters


import androidx.recyclerview.widget.DiffUtil
import com.bregandert.filmsearch.domain.Film


class FilmDiff(val oldList: List<Film>, val newList: List<Film>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFilm = oldList[oldItemPosition]
        val newFilm = newList[newItemPosition]
        return oldFilm.title == newFilm.title &&
                oldFilm.description == newFilm.description &&
                oldFilm.poster == newFilm.poster
    }



}