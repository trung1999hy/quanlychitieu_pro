package com.example.quanlychitieu.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

import com.example.quanlychitieu.ui.MainActivity
import com.thn.quanlychitieu.R

class AlarmReceiver : BroadcastReceiver() {
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationCompat: NotificationCompat.Builder
    private val CHANNEL_ID = " 100"
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "FOO_ACTION") {
            val intent = Intent(context, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            val pendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = context?.getString(R.string.post_notifi)
                val descriptionText = context?.getString(R.string.channel_post_notifi)
                val importance = NotificationManager.IMPORTANCE_DEFAULT

                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                notificationManager =
                    context?.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)

            }
            context?.let {
                notificationCompat = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Thông báo !")
                    .setContentText("Bạn có nhắc nhở làm xin vui lòng kiểm tra")
                    .setContentIntent(pendingIntent)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                notificationCompat.build()
                notificationManager.notify(
                    System.currentTimeMillis().toInt(),
                    notificationCompat.build()
                )
            }
        }

    }
}
