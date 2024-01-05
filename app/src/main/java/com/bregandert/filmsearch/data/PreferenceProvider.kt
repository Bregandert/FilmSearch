package com.bregandert.filmsearch.data

import android.content.Context
import android.content.SharedPreferences

//Работает с SharedPrefereces приложения
class PreferenceProvider(context: Context) {

//    Контекст приложения
    private val appContext = context.applicationContext

//    SharedPreferences
    private val preference: SharedPreferences = appContext.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)

//    Инициализация SharedPreferences при первом запуске приложения
    init {
        if (preference.getBoolean(KEY_FIRST_LAUNCH, false)) {
            preference.edit().putString(KEY_DEFAULT_CATEGORY, DEFAULT_CATEGORY).apply()
            preference.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply()
            saveLastAPIRequestTime()
        }
    }

//    Категория по умолчанию
    fun saveDefaultCategory(category: String) {
        preference.edit().putString(KEY_DEFAULT_CATEGORY, category).apply()
    }

    fun getDefaultCategory() :String {
        return preference.getString(KEY_DEFAULT_CATEGORY, DEFAULT_CATEGORY) ?: DEFAULT_CATEGORY
    }

//    время в мс, когда последний раз осуществлялся доступ к удаленному API
    fun saveLastAPIRequestTime() {
        val time = System.currentTimeMillis()
        preference.edit().putLong(KEY_LAST_DOWNLOAD_TIME, time).apply()
    }

    fun getLastAPIRequestTime(): Long {
        return preference.getLong(KEY_LAST_DOWNLOAD_TIME, 0L)
    }

//    Какая категория была сохранена в db
    fun saveCategoryInDB(category: String) {
        preference.edit().putString(KEY_LOCAL_DB_CATEGORY, category).apply()
    }

    fun getCategoryInDB(): String {
        return preference.getString(KEY_LOCAL_DB_CATEGORY, DEFAULT_DB_CATEGORY) ?: DEFAULT_DB_CATEGORY
    }

    companion object {
        private const val SETTINGS = "settings"
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_DEFAULT_CATEGORY = "default_category"
        private const val DEFAULT_CATEGORY = "popular"
        private const val KEY_LAST_DOWNLOAD_TIME = "last_time"
        private const val KEY_LOCAL_DB_CATEGORY = "db_category"
        private const val DEFAULT_DB_CATEGORY = "none"
    }

}