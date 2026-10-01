package com.example.data.location

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.WafaQuickEatsApp
import com.example.WafaQuickEatsApp.Companion.LOCATION_CHANNEL_ID

class RiderLocationService : Service(), LocationListener {
    private var locationManager: LocationManager? = null
    private var orderId: String = ""
    private var riderId: String = ""

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        locationManager = getSystemService(Context.LOCATION_SERVICE) as? LocationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        if (action == ACTION_STOP) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
            return START_NOT_STICKY
        }

        orderId = intent?.getStringExtra(EXTRA_ORDER_ID) ?: "WQE-ORD-ACTIVE"
        riderId = intent?.getStringExtra(EXTRA_RIDER_ID) ?: "WQE-RDR-ACTIVE"

        val notification = buildNotification("Starting GPS tracking for Order $orderId...")
        startForeground(NOTIFICATION_ID, notification)

        startLocationTracking()

        return START_STICKY
    }

    @SuppressLint("MissingPermission")
    private fun startLocationTracking() {
        try {
            locationManager?.let { lm ->
                if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    lm.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        3000L, // 3 seconds
                        5.0f,  // 5 meters
                        this
                    )
                }
                if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    lm.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        5000L,
                        10.0f,
                        this
                    )
                }
            }
        } catch (e: SecurityException) {
            // Permission check handled gracefully
        }
    }

    override fun onLocationChanged(location: Location) {
        val app = application as? WafaQuickEatsApp ?: return
        val speedKmh = location.speed * 3.6f

        // Record real GPS in repository (stores in Room + syncs to backend)
        app.repository.recordGpsLocation(
            orderId = orderId,
            riderId = riderId,
            lat = location.latitude,
            lng = location.longitude,
            accuracy = location.accuracy,
            speed = speedKmh,
            bearing = location.bearing
        )

        // Update foreground notification with live stats
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        val updatedNotification = buildNotification(
            "⚡ Live Delivery: %.1f km/h | Acc: %.0fm".format(speedKmh, location.accuracy)
        )
        notificationManager.notify(NOTIFICATION_ID, updatedNotification)
    }

    private fun buildNotification(contentText: String): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, LOCATION_CHANNEL_ID)
            .setContentTitle("Wafa QuickEats ⚡ Rider GPS Active")
            .setContentText(contentText)
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager?.removeUpdates(this)
    }

    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
    @Deprecated("Deprecated in Java")
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    companion object {
        const val NOTIFICATION_ID = 4040
        const val ACTION_START = "com.example.action.START_GPS"
        const val ACTION_STOP = "com.example.action.STOP_GPS"
        const val EXTRA_ORDER_ID = "extra_order_id"
        const val EXTRA_RIDER_ID = "extra_rider_id"

        fun startService(context: Context, orderId: String, riderId: String) {
            val intent = Intent(context, RiderLocationService::class.java).apply {
                action = ACTION_START
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(EXTRA_RIDER_ID, riderId)
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stopService(context: Context) {
            val intent = Intent(context, RiderLocationService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }
    }
}
