package com.bregandert.filmsearch.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Film(

    val title: String,
    val poster: String,
    val description: String,
    var rating: Double = 0.0,
    var isFavorite: Boolean = false
) : Parcelable