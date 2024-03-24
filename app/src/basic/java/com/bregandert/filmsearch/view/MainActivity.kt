import android.Manifest
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.bregandert.filmsearch.App
import com.bregandert.filmsearch.R
import com.bregandert.filmsearch.databinding.ActivityMainBinding
import com.bregandert.filmsearch.databinding.FilmItemBinding
import com.bregandert.filmsearch.data.entity.Film
import com.bregandert.filmsearch.utils.BatteryReceiver
import com.bregandert.filmsearch.view.fragments.DetailsFragment
import com.bregandert.filmsearch.view.fragments.FavoritesFragment
import com.bregandert.filmsearch.view.fragments.HomeFragment
import com.bregandert.filmsearch.view.fragments.SelectionsFragment
import com.bregandert.filmsearch.view.fragments.SettingsFragment
import com.bregandert.filmsearch.view.fragments.WatchLaterFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var backPressed = 0L
    private lateinit var receiver : BroadcastReceiver



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        Инициализируем слушателей меню навигации
        initNavigation()
//        инициализируем нашу ДБ
//        initFilmsDB()

        receiver = BatteryReceiver()

//        Регистрируем Receiver для реакции на низкий заряд батареии и подключение кабеля
        val filters = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(Intent.ACTION_POWER_CONNECTED)
        }
        registerReceiver(receiver, filters)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 99)
        }

        val film =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent?.getParcelableExtra(App.instance.FILM, Film::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        intent?.getParcelableExtra(App.instance.FILM) as Film?
                    }

        if (film == null) {
//            запускаем home fragment
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_placeholder, HomeFragment())
                .addToBackStack(App.instance.FRAGMENT_TAG)
                .commit()
        } else {
            launchDetailsFragment(film)
        }

        }



    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }





    // Check back pressed. If 2 times in less than 2 sec om main screen - exit. If not main screen (fragment) - back.
    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount <= 1) {
            if (backPressed + App.instance.BACK_CLICK_TIME_INTERVAL > System.currentTimeMillis()) {
                onBackPressedDispatcher.onBackPressed()
                finish()
            } else {
                Toast.makeText(this, getString(R.string.double_back_toast), Toast.LENGTH_SHORT).show()
            }

            backPressed = System.currentTimeMillis()
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    // Contains constant - interval for double tap in ms

    private fun initNavigation() {

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    val tag = "home"
                    val fragment = checkFragmentExistence(tag)
                    //В первом параметре, если фрагмент не найден и метод вернул null, то с помощью
                    //элвиса мы вызываем создание нового фрагмента
                    changeFragment( fragment?: HomeFragment(), tag)
                    true
                }
                R.id.favorites -> {
                    Toast.makeText(this, "Доступно в Pro версии", Toast.LENGTH_SHORT).show()
                    true
                }


                R.id.settings -> {
                    val tag = "settings"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment( fragment?: SettingsFragment(), tag)
                    true
                }
                else -> false
            }
        }
    }

    //Ищем фрагмент по тэгу, если он есть то возвращаем его, если нет - то null
    private fun checkFragmentExistence(tag: String): Fragment? = supportFragmentManager.findFragmentByTag(tag)

    private fun changeFragment(fragment: Fragment, tag: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment, tag)
            .addToBackStack(null)
            .commit()
    }

    fun launchDetailsFragment(film: Film) {
        val bundle = Bundle()
        bundle.putParcelable(App.instance.FILM, film)
        val fragment = DetailsFragment()
        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment)
            .addToBackStack(App.instance.FRAGMENT_TAG)
            .commit()
    }
}


