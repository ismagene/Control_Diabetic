package com.ismasoft.controldiabetic.utilities

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.ismasoft.controldiabetic.R

class RebootServiceClass: JobIntentService() {
    /**
     * Creates an IntentService. Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    override fun onHandleWork(intent: Intent) {
        var intentType = intent?.getExtras()?.getString("caller")
        if (intentType == null) return
        if (intentType == "RebootReceiver")
        {
            var settings = getSharedPreferences(getString(R.string.sharedControlAlarmes), Context.MODE_PRIVATE)
            // Recuperem les alarmes definim un numero maxim de 200 alarmes, no es poden posar mes alarmes que minuts
            for (i in 1..1440){
                if(settings.getInt("alarmID${i}", 0) !=  0){



                    setAlarm(settings.getInt("alarmID${i}", 0), settings.getLong("alarmTime${i}", 0), this)
                }
                else break
            }
        }
    }

}