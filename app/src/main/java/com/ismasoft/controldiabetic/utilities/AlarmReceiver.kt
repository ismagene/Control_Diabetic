package com.ismasoft.controldiabetic.utilities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val service = Intent(context, NotificationService::class.java)
        service.setData((Uri.parse("custom://" + System.currentTimeMillis())))
        ContextCompat.startForegroundService(context, service)
        Log.d("alarmaSet", " Alarma rebuda!")
    }
}