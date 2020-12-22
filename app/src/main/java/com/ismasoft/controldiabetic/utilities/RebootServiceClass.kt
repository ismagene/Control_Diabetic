package com.ismasoft.controldiabetic.utilities

import android.app.IntentService
import android.app.Notification
import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.ismasoft.controldiabetic.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

class RebootServiceClass: IntentService {
    /**
     * Creates an IntentService. Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    constructor(name:String) : super(name) {
        startForeground(1, Notification())
    }
    constructor() : super("RebootServiceClass") {}
    override fun onCreate() {
        super.onCreate()
    }
    override fun onHandleIntent(intent: Intent?) {
        var intentType = intent?.getExtras()?.getString("caller")
        if (intentType == null) return
        if (intentType == "RebootReceiver")
        {
            var settings = getSharedPreferences(getString(R.string.sharedControlAlarmes), Context.MODE_PRIVATE)
            // Recuperem les alarmes definim un numero maxim de 200 alarmes, no es poden posar mes alarmes que minuts
            for (i in 1..1440){
                if(settings.getInt("alarmID${i}", 0) !=  0){

                    var parts = settings.getString("alarmTime${i}","")?.split(":")
                    var hora = parts?.get(0)?.toInt()
                    var minuts = parts?.get(1)?.toInt()

                    var calendar = Calendar.getInstance()
                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                    var dataString = sdf.format(Date())
                    parts = dataString.split("/")
                    var day =  parts[0].toInt()
                    var month = parts[1].toInt()-1
                    var year = parts[2].toInt()

                    calendar.set(year, month, day, hora!!, minuts!!, 0)

                    // Si la data de l'alarma amb la data d'avui ja ha passat, sumarem un dia al calendari perquè no apareixin automaticament les alarmes pasades cada cop que entrem.
                    var calendarComparar = Calendar.getInstance()
                    if(calendarComparar.after(calendar)){
                        calendar.add(Calendar.DAY_OF_YEAR, +1)
                    }

                    setAlarm(settings.getInt("alarmID${i}", 0), calendar.timeInMillis, this)
                }
                else break
            }

            // restaurar alarma visita
            settings = getSharedPreferences("sharedAlarmaVisita", Context.MODE_PRIVATE)
            var horaAlarmaVisita = settings.getString("enviarVisita",null)
            if(horaAlarmaVisita != null) {
                var calendar = horaAlarmaVisita.toLong()
                var calendarComparar = Calendar.getInstance()
                if(calendarComparar.timeInMillis < calendar) {
                    setAlarmVisita(10000, calendar, this)
                }
            }

        }
    }

}