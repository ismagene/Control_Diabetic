package com.ismasoft.controldiabetic.utilities

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.widget.Toast

class SendDataService : Service() {
    private val mBinder: LocalBinder = LocalBinder()
    protected var handler: Handler? = null
    protected var mToast: Toast? = null

    inner class LocalBinder : Binder() {
        val service: SendDataService
            get() = this@SendDataService
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        handler = Handler()
        handler!!.post {

            Thread.sleep(5000)
            Toast.makeText(this, "L'hora és obligatoria per guardar l'alarma", Toast.LENGTH_SHORT).show()
        }
        return START_STICKY
    }
}