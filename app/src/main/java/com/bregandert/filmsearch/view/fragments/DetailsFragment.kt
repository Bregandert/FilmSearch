package com.bregandert.filmsearch.view.fragments

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.bregandert.filmsearch.App

import com.bregandert.filmsearch.R
import com.bregandert.filmsearch.databinding.FragmentDetailsBinding
import com.bregandert.filmsearch.data.entity.Film
import com.bregandert.filmsearch.utils.NotificationService
import com.bregandert.filmsearch.viewmodel.DetailsFragmentViewModel
import com.bregandert.retrofit.entity.ApiConstants
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var film: Film
    private lateinit var notificationService: NotificationService
    private val scope = CoroutineScope(Dispatchers.IO)
    private val viewModel: DetailsFragmentViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(layoutInflater)

        notificationService = NotificationService(requireContext().applicationContext)

        film = (
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arguments?.getParcelable(App.instance.FILM, Film::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    arguments?.getParcelable(App.instance.FILM) as Film?
                }
                ) ?: return binding.root
        initDetails()

        setFavoriteIcon()
        setShareIcon()
        setDownloadFAB()

        return binding.root
    }

    private fun initDetails() {
        //Устанавливаем заголовок
        binding.detailsToolbar.title = film.title
        //Устанавливаем постер
        Glide.with(this)
            .load(ApiConstants.IMAGES_URL + "w780" + film.poster)
            .centerCrop()
            .into(binding.detailsPoster)
//        binding.detailsPoster.setImageResource(film.poster)
        //Устанавливаем описание
        binding.detailsDescription.text = film.description
        binding.detailsFabWatchLater.setOnClickListener{
            notificationService.sendFilmnotification(film)
        }
    }

    private fun setDownloadFAB() {
        binding.detailsFabDownloadWp.setOnClickListener {
            performAsyncLoadOfPoster()
        }
    }

    private fun setFavoriteIcon() {

        binding.favoritesFab.setOnClickListener {
            film.isFavorite = !film.isFavorite
            setFavoriteIcon()
        }

        binding.favoritesFab.setImageResource(
            if (film.isFavorite) R.drawable.ic_favorite
            else R.drawable.ic_favorite_border
        )

//        // changing 'add to favorites' fab icon depending on status
//        binding.favoritesFab.setImageResource(
//            if (film.isFavorite) R.drawable.ic_favorite
//            else R.drawable.ic_favorite_border
//        )
//        // setting 'add to favorites' fab click listener
//        binding.favoritesFab.setOnClickListener {
//            if (!film.isFavorite) {
//                binding.favoritesFab.setImageResource(R.drawable.ic_favorite)
//                film.isFavorite = true
//            } else {
//                binding.favoritesFab.setImageResource(R.drawable.ic_favorite_border)
//                film.isFavorite = false
//            }
//        }
    }

    private fun setShareIcon() {
        // setting share fab click listener
        binding.detailsFab.setOnClickListener {
            //Создаем интент
            val intent = Intent()
            //Указываем action с которым он запускается
            intent.action = Intent.ACTION_SEND
            //Кладем данные о нашем фильме
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Check out this film: ${film.title} \n\n ${film.description}"
            )
            //Указываем MIME тип, чтобы система знала, какое приложения предложить
            intent.type = "text/plain"
            //Запускаем наше активити
            startActivity(Intent.createChooser(intent, "Share To:"))
        }
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            1
        )
    }

//    Пишем логику сохранения в галерею. Делается через MediaStore

    private fun saveToGallery(bitmap: Bitmap) {
//        проверяем версию системы
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            Создаем объект для передачи данных
            val contentValues = ContentValues().apply {
//            Составляем информацию для файла
//            (имя, тип, дата создания, куда сохранять и т.д.)
                put(MediaStore.Images.Media.TITLE, film.title.handleSingleQuote())
                put(MediaStore.Images.Media.DISPLAY_NAME, film.title.handleSingleQuote())
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
                put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/FilmSearch")
            }

//            Получаем ссылку на объект Content resolver,
//            который помогает передавать информацию вовне
            val contentResolver = requireActivity().contentResolver
            val uri = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )

//            открываем канал для записи на диск
            val outputStream = contentResolver.openOutputStream(uri!!)
//            передаем нашу картинку, может сделать компрессию
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream!!)
//            закрываем поток
            outputStream?.close()
        } else {
//            то же, но на более старых версиях ОС
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.insertImage(
                requireActivity().contentResolver,
                bitmap,
                film.title.handleSingleQuote(),
                film.description.handleSingleQuote()
            )
        }
    }

    private fun String.handleSingleQuote(): String {
        return this.replace("'", "")
    }

    private fun performAsyncLoadOfPoster() {
//        проверяем есть ли разрешение и версию ОС
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU && !checkPermission()) {
//            запрашиваем разрешение если его нет
            requestPermission()
            return
        }
        MainScope().launch {
//            включаем Прогресс-бар
            binding.progressBar.isVisible = true
//            сjздаем через async, так как нам нужен результат от работы. то есть Bitmap
            val job = scope.async {
                viewModel.loadWallpaper(ApiConstants.IMAGES_URL + "original" + film.poster)
            }
            val result = job.await()
//            если загрузка провалена
            if (result == null) {
                Toast.makeText(binding.root.context, R.string.api_error_message, Toast.LENGTH_SHORT).show()

            } else {
//                если картинка загрузилась нормально, сохраняем в галерею
                saveToGallery(result)
//                выводим снэкбар с кнопкой перейти в галерею
                Snackbar.make(
                    binding.root,
                    R.string.downloaded_to_gallery,
                    Snackbar.LENGTH_LONG
                ).setAction(R.string.open) {
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.type = "image/*"
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }.show()
//                отключаем Прогресс-бар
                binding.progressBar.isVisible = false
            }
        }
    }

//      создаем родительский скоуп с диспатчером Main потока, так как будем взаимодействовать с UI





}