package com.childsafety.monitor.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.childsafety.monitor.MainActivity
import com.childsafety.monitor.utils.PreferenceManager
import com.childsafety.monitor.websocket.WebSocketManager
import kotlinx.coroutines.*

class MonitoringService : Service() {
    
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var webSocketManager: WebSocketManager
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    companion object {
        const val NOTIFICATION_ID = 1001
        const val CHANNEL_ID = "monitoring_channel"
    }
    
    override fun onCreate() {
        super.onCreate()
        preferenceManager = PreferenceManager(this)
        webSocketManager = WebSocketManager(this)
        
        startForeground(NOTIFICATION_ID, createNotification())
        
        // Connect to WebSocket
        serviceScope.launch {
            webSocketManager.connect()
            startMonitoring()
        }
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        webSocketManager.disconnect()
    }
    
    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Child Safety Active")
            .setContentText("Monitoring is running in background")
            .setSmallIcon(android.R.drawable.ic_menu_view)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }
    
    private suspend fun startMonitoring() {
        while (isActive) {
            // Collect and send monitoring data
            collectAndSendData()
            delay(30000) // Every 30 seconds
        }
    }
    
    private suspend fun collectAndSendData() {
        val data = mapOf(
            "type" to "monitoring_update",
            "deviceId" to preferenceManager.getDeviceId(),
            "childName" to preferenceManager.getChildName(),
            "location" to getLocation(),
            "appUsage" to getAppUsage(),
            "screenTime" to getScreenTime(),
            "timestamp" to System.currentTimeMillis()
        )
        
        webSocketManager.sendMessage(data)
    }
    
    private fun getLocation(): Map<String, Any> {
        // Implement location tracking
        return mapOf(
            "latitude" to 0.0,
            "longitude" to 0.0,
            "accuracy" to 0.0
        )
    }
    
    private fun getAppUsage(): List<Map<String, Any>> {
        // Implement app usage tracking
        return emptyList()
    }
    
    private fun getScreenTime(): Map<String, Any> {
        // Implement screen time tracking
        return mapOf(
            "today" to 0L,
            "thisWeek" to 0L
        )
    }
}
