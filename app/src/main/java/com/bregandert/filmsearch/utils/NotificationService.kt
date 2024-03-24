package com.bregandert.filmsearch.utils

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getString
import androidx.core.content.ContextCompat.getSystemService
import com.bregandert.filmsearch.App
import com.bregandert.filmsearch.R
import com.bregandert.filmsearch.data.entity.Film
import com.bregandert.filmsearch.view.MainActivity
import com.bregandert.filmsearch.view.fragments.DetailsFragment
import java.util.Calendar

class NotificationService(val context: Context) {

    private val CHANNEL_ID = "FilmSearch Notification channel"
    private val CHANNEL_NAME = "Film Search"
    private val CHANNEL_DESCRIPTION = "FilmSearch Notification channel"
    private val filmNotificationTitle = getString(context, R.string.notification_title)
    private val filmNotificationText = getString(context, R.string.notification_text)
    private val importance = NotificationManager.IMPORTANCE_DEFAULT
    private var notificationManager: NotificationManager? = null
    private val icon: Int = R.drawable.ic_baseline_watch_later_24

    init {
        notificationManager = getSystemService(context, NotificationManager::class.java)

//        Создаем NotificationChannel, но только для API 26+
//        более старыми версиями не поддерживается
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
            }

//            Регистрируем канал в системе
            notificationManager?.createNotificationChannel(channel)
        }

    }

    fun sendNotification(id: Int, title: String, text: String, pendingIntent: PendingIntent) {
        val notification: Notification

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setShowWhen(true)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build()
        } else {
            notification = Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(icon)
                .setContentIntent(pendingIntent)
                .setShowWhen(true)
                .setAutoCancel(true)
                .build()
        }

        notificationManager?.notify(id, notification)
    }

    fun sendFilmnotification(film: Film) {
        val id = 1
        var dateTimeInMillis: Long = 0L
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(App.instance.FILM, film)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        sendNotification(id, filmNotificationTitle, filmNotificationText + film.title, pendingIntent)

    }

    fun setFilmNotification(film: Film) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        DatePickerDialog(
            context,
            { _, dpdYear, dpdMonth, dayOfMonth ->
                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener { _, hourOfDay, pickerMinute ->
                        val pickedDateTime = Calendar.getInstance()
                        pickedDateTime.set(
                            dpdYear,
                            dpdMonth,
                            dayOfMonth,
                            hourOfDay,
                            pickerMinute,
                            0
                        )
                        val dateTimeInMillis = pickedDateTime.timeInMillis
                        createWatchLaterEvent(context, dateTimeInMillis, film)
                    }

                TimePickerDialog(
                    context,
                    timeSetListener,
                    currentHour,
                    currentMinute,
                    true
                ).show()

            },
            currentYear,
            currentMonth,
            currentDay
        ).show()
    }

    private fun createWatchLaterEvent(context: Context, dateTimeInMillis: Long, film: Film) {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(film.title, null, context, WatchLaterReminderReceiver()::class.java)
        intent.putExtra(App.instance.FILM, film)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            dateTimeInMillis,
            pendingIntent
        )

    }


}