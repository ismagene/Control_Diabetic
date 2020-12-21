package com.ismasoft.controldiabetic.utilities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat

class AlarmVisitaReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        var service = Intent(context, EnviamentAutoHistoricService::class.java)
        service.setData((Uri.parse("custom://" + System.currentTimeMillis())))
        ContextCompat.startForegroundService(context, service)
        Log.d("alarmaVisitaSet", " Alarma visita rebuda!")
    }
}