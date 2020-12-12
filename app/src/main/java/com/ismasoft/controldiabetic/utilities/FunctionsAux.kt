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
//import io.javalin.Javalin
//import org.apache.commons.mail.DefaultAuthenticator
//import org.apache.commons.mail.SimpleEmail
import org.jetbrains.anko.contentView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.days
import kotlin.time.hours

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

fun getDataHours(hora : Int, minuts : Int): Date {
    val calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat("dd/MM/yyyy")
    var dataString = sdf.format(Date())
    var parts = dataString.split("/")
    var day =  parts[0].toInt()
    var month = parts[1].toInt()-1
    var year = parts[2].toInt()
    calendar.set(year, month, day, hora, minuts, 0)
    return calendar.time
}

/** Funció que seteja les alarmes i les programa repetitives cada dia a la mateixa hora */
fun setAlarm(posicioAlarma: Int, timestamp:Long, ctx:Context) {
    var alarmManager = ctx.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
    var alarmIntent = Intent(ctx, AlarmReceiver::class.java)
//    alarmIntent.setData((Uri.parse("custo://" + System.currentTimeMillis())))
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


/** Funció per enviar un mail automaticament quan ens registrem */
//fun mailConfirmacioRegistre(email : String) {
//
//    val app = Javalin.create().start(7070)
//
//    app.get("/") { ctx ->
//        ctx.html("""
//            <form action="/contact-us" method="post">
//                <input name="subject" placeholder="Subject">
//                <br>
//                <textarea name="message" placeholder="Your message ..."></textarea>
//                <br>
//                <button>Submit</button>
//            </form>
//    """.trimIndent())
//    }
//
//    app.post("/contact-us") { ctx ->
//        SimpleEmail().apply {
//            setHostName("smtp.googlemail.com")
//            setSmtpPort(465)
//            setAuthenticator(DefaultAuthenticator("controldiabeticsuport@gmail.com", "cdsuport2020"))
//            setSSLOnConnect(true)
//            setFrom("YOUR_EMAIL")
//            setSubject(ctx.formParam("subject"))
//            setMsg(ctx.formParam("message"))
//            addTo(email)
//        }.send() // will throw email-exception if something is wrong
//        ctx.redirect("/contact-us/success")
//    }
//
//    app.get("/contact-us/success") { ctx -> ctx.html("Your message was sent") }
//
//}

