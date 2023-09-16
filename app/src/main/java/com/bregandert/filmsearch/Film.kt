package com.bregandert.filmsearch

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Film(
    val id: Int,
    val title: String,
    val poster: Int,
    val description: String,
    var isFavorite: Boolean = false
) : Parcelable