package com.example

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.data.repository.WafaRepository

class WafaQuickEatsApp : Application() {
    lateinit var repository: WafaRepository
        private set

    override fun onCreate() {
        super.onCreate()
        repository = WafaRepository(this)
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                LOCATION_CHANNEL_ID,
                "Wafa Live Delivery GPS",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows live GPS broadcast status for active Wafa QuickEats deliveries"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    companion object {
        const val LOCATION_CHANNEL_ID = "wafa_rider_gps_channel"
    }
}
