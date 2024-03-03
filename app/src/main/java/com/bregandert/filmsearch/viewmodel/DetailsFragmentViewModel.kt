package com.bregandert.filmsearch.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import com.bregandert.filmsearch.App
import com.bregandert.filmsearch.domain.Interactor
import java.net.URL
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DetailsFragmentViewModel: ViewModel() {

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
    }
    suspend fun loadWallpaper(url: String): Bitmap? {
        return suspendCoroutine {
            val url = URL(url)
            val bitmap: Bitmap? = try {
                BitmapFactory.decodeStream(url.openConnection().getInputStream())
            } catch (e: Exception) {
                null
            }
            it.resume(bitmap)
        }
    }
}