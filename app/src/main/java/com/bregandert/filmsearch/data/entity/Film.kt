package com.bregandert.filmsearch.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "cashed_films", indices = [Index(value = ["title"], unique = true)])
data class Film(

    @PrimaryKey(autoGenerate = true) val id: Int =0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "poster_path") val poster: String,
    @ColumnInfo(name = "overview") val description: String,
    @ColumnInfo(name = "vote_average") var rating: Double = 0.0,
    var isFavorite: Boolean = false
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Film

        if (poster != other.poster) return false
        if (title != other.title) return false
        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = poster.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        return result
    }
}