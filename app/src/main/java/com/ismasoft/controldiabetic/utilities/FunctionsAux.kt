package com.ismasoft.controldiabetic.utilities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

/** Funció per amagar el teclat virtual */
fun hideKeyboard(context: Context) {
    val imm = context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow((context as AppCompatActivity).findViewById<ViewGroup>(android.R.id.content).windowToken, 0)

}

/** Funció que converteix la data Timestamp de firebase a Date */
fun convertirADateLaDataFirebase(dataFirebase : Timestamp): Date {
    return Date(dataFirebase.seconds * 1000 + dataFirebase.nanoseconds / 1000000)
}

fun getDataSenseHora(data: Date): Calendar {
    val calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat("dd/MM/yyyy")
    var dataString = sdf.format(data)
    var parts = dataString.split("/")
    var day =  parts[0].toInt()
    var month = parts[1].toInt()-1
    var year = parts[2].toInt()
    calendar.set(year, month, day, 0, 0, 0,)
    return calendar
}

/** Funció que seteja les alarmes i les programa repetitives cada dia a la mateixa hora */
fun setAlarm(posicioAlarma: Int, timestamp:Long, ctx:Context) {
    var alarmManager = ctx.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
    var alarmIntent = Intent(ctx, AlarmReceiver::class.java)
    alarmIntent.setData((Uri.parse("custo://" + System.currentTimeMillis())))
    var pendingIntent = PendingIntent.getBroadcast(ctx, posicioAlarma, alarmIntent, 0)
    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        timestamp,
        AlarmManager.INTERVAL_DAY,
        pendingIntent
    )
}

/** Funció que cancela les alarmes que eliminiem */
fun deleteAlarm(posicioAlarma:Int, ctx:Context) {
    val alarmManager = ctx.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
    val alarmIntent = Intent(ctx, AlarmReceiver::class.java)
    val pendingIntent: PendingIntent
    pendingIntent = PendingIntent.getBroadcast(ctx, posicioAlarma, alarmIntent,0)
    alarmManager.cancel(pendingIntent)
}


/** Funció que seteja la alarma per fer l'enviament automàtic del historial de glucosa */
fun setAlarmVisita(posicioAlarma: Int, timestamp:Long, ctx:Context) {
    var alarmManager = ctx.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
    var alarmIntent = Intent(ctx, AlarmVisitaReceiver::class.java)
    alarmIntent.setData((Uri.parse("custo://" + System.currentTimeMillis())))
    var pendingIntent = PendingIntent.getBroadcast(ctx, posicioAlarma, alarmIntent, 0)
    alarmManager.setExact(
        AlarmManager.RTC_WAKEUP,
        timestamp,
        pendingIntent
    )
}

/** Funció que cancela la alarma de la visita */
fun deleteVisitaAlarm(posicioAlarma:Int, ctx:Context) {
    val alarmManager = ctx.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
    val alarmIntent = Intent(ctx, AlarmVisitaReceiver::class.java)
    val pendingIntent: PendingIntent
    pendingIntent = PendingIntent.getBroadcast(ctx, posicioAlarma, alarmIntent,0)
    alarmManager.cancel(pendingIntent)
}