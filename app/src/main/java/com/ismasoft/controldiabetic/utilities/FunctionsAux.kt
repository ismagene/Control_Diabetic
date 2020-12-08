package com.ismasoft.controldiabetic.utilities

import android.content.Context
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import org.jetbrains.anko.contentView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.days
import kotlin.time.hours

fun hideKeyboard(context: Context) {
    val imm = context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow((context as AppCompatActivity).findViewById<ViewGroup>(android.R.id.content).windowToken, 0)

}

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

fun getDataCalendar(data: Date): Calendar {
    val calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat("dd/MM/yyyy")
    var dataString = sdf.format(data)
    var parts = dataString.split("/")
    var day =  parts[0].toInt()
    var month = parts[1].toInt()-1
    var year = parts[2].toInt()
    var hour = data.hours
    var minute = data.minutes
    calendar.set(year, month, day, hour, minute, 0)
    return calendar
}