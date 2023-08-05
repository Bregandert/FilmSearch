package com.bregandert.filmsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bregandert.filmsearch.databinding.ActivityMainBinding

//import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()

//        setContentView(R.layout.activity_main)
//        initButtons()


    }

    private fun initNavigation() {

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    Toast.makeText(this, "Настройки", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }

        binding.navyAppBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.favorites -> {
                    Toast.makeText(this, "Избранное", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.watch_later -> {
                    Toast.makeText(this, "Смотреть позже", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.selections -> {
                    Toast.makeText(this, "Подборки", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false

            }


        }
    }
}

//    private fun initButtons() {
//        binding.btnMenu.setOnClickListener {
//            Toast.makeText(this, "Меню", Toast.LENGTH_SHORT).show()
//        }
//        binding.btnFavorites.setOnClickListener {
//            Toast.makeText(this, "Избранное", Toast.LENGTH_SHORT).show()
//        }
//        binding.btnWatchLater.setOnClickListener {
//            Toast.makeText(this, "Посмотреть позже", Toast.LENGTH_SHORT).show()
//        }
//        binding.btnSelections.setOnClickListener {
//            Toast.makeText(this, "Подборки", Toast.LENGTH_SHORT).show()
//        }
//        binding.btnSettings.setOnClickListener {
//            Toast.makeText(this, "Настройки", Toast.LENGTH_SHORT).show()
//        }
//    }
