package com.example.beekeeper.data.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log.d
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.beekeeper.R
import com.example.beekeeper.presenter.screen.main.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        d("onMessageReceived", message.data.toString())
        showNotification()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

    }

    private fun showNotification(){

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            putExtra("openFragment", "SuggestionsFragment")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        // Wrap the Intent in a PendingIntent
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(applicationContext, "channel_analytics")
            .setContentTitle("Suggestions")
            .setContentText("Check out daily recommendations and suggestions!")
            .setContentIntent(pendingIntent)
            .setColor(ContextCompat.getColor(applicationContext, R.color.honey))
            .setSmallIcon(R.drawable.ic_beekeper_65)
            .build()

        if(ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED )
            NotificationManagerCompat.from(applicationContext)
                .notify(1, notification)

    }


}