package com.ismasoft.controldiabetic.utilities

import android.app.IntentService
import android.app.Notification
import android.content.Context
import android.content.Intent
import com.ismasoft.controldiabetic.R

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
        val intentType = intent?.getExtras()?.getString("caller")
        if (intentType == null) return
        if (intentType == "RebootReceiver")
        {
            val settings = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
            // Recuperem les alarmes
            for (i in 1..200){
                if(settings.getInt("alarmID", 0) !=  null){
                    setAlarm(settings.getInt("alarmID", 0), settings.getLong("alarmTime", 0), this)
                }
            }
        }
    }
}