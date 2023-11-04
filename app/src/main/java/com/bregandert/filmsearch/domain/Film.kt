package com.bregandert.filmsearch.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Film(
    val id: Int,
    val title: String,
    val poster: Int,
    val description: String,
    var rating: Float = 0f,
    var isFavorite: Boolean = false
) : Parcelable