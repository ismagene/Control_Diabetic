package com.ismasoft.controldiabetic.utilities

import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.ismasoft.controldiabetic.R
import com.ismasoft.controldiabetic.ui.activities.AfegirAlarmaActivity
import com.ismasoft.controldiabetic.ui.activities.AfegirControlActivity

class NotificationService: IntentService {
    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    internal lateinit var notification: Notification
    constructor(name:String) : super(name) {}
    constructor() : super("SERVICE") {}

    @TargetApi(Build.VERSION_CODES.O)
    override fun onHandleIntent(intent: Intent?) {
        val NOTIFICATION_CHANNEL_ID = getApplicationContext().getString(R.string.app_name)  // Titol de la notificació
        val context = this.getApplicationContext()
        var notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mIntent = Intent(this, AfegirControlActivity::class.java)
        val res = this.getResources()
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val message = getString(R.string.new_notification)  // missatge de la notificació

        // Per versions per sobre de android O
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val NOTIFY_ID = 0 // ID of notification
            val id = NOTIFICATION_CHANNEL_ID // default_channel_id
            val title = NOTIFICATION_CHANNEL_ID // Default Channel
            val pendingIntent:PendingIntent
            val builder: NotificationCompat.Builder
            var notifManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (notifManager == null)
            {
                notifManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            val importance = NotificationManager.IMPORTANCE_HIGH
            var mChannel = notifManager.getNotificationChannel(id)
            if (mChannel == null)
            {
                mChannel = NotificationChannel(id, title, importance)
                mChannel.enableVibration(true)
                mChannel.setVibrationPattern(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
                notifManager.createNotificationChannel(mChannel)
            }
            builder = NotificationCompat.Builder(context, id)
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            builder.setContentTitle(getString(R.string.app_name)).setCategory(Notification.CATEGORY_SERVICE)
                .setSmallIcon(R.drawable.icon_alarm) // required
                .setContentText(message)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.icon_alarm))
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
                .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
            val notification = builder.build()
            notifManager.notify(NOTIFY_ID, notification)
            startForeground(1, notification)
        }
        else
        {
            pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            notification = NotificationCompat.Builder(this)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.icon_alarm)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.icon_alarm))
                .setSound(soundUri)
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.app_name)).setCategory(Notification.CATEGORY_SERVICE)
                .setContentText(message).build()
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }
    companion object {
        private val NOTIFICATION_ID = 1
    }


}