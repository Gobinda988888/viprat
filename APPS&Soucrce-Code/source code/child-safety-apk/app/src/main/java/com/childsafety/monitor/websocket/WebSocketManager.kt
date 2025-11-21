package com.childsafety.monitor.websocket

import android.content.Context
import android.util.Log
import com.childsafety.monitor.utils.PreferenceManager
import com.google.gson.Gson
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class WebSocketManager(private val context: Context) {
    
    private var webSocketClient: WebSocketClient? = null
    private val preferenceManager = PreferenceManager(context)
    private val gson = Gson()
    
    companion object {
        private const val TAG = "WebSocketManager"
    }
    
    fun connect() {
        val serverUrl = preferenceManager.getServerUrl()
        if (serverUrl.isEmpty()) {
            Log.e(TAG, "Server URL not configured")
            return
        }
        
        try {
            val uri = URI(serverUrl)
            webSocketClient = object : WebSocketClient(uri) {
                override fun onOpen(handshakedata: ServerHandshake?) {
                    Log.d(TAG, "WebSocket connected")
                    sendRegistrationMessage()
                }
                
                override fun onMessage(message: String?) {
                    Log.d(TAG, "Message received: $message")
                    handleMessage(message)
                }
                
                override fun onClose(code: Int, reason: String?, remote: Boolean) {
                    Log.d(TAG, "WebSocket closed: $reason")
                    // Attempt reconnection
                    reconnect()
                }
                
                override fun onError(ex: Exception?) {
                    Log.e(TAG, "WebSocket error", ex)
                }
            }
            
            webSocketClient?.connect()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to connect", e)
        }
    }
    
    fun disconnect() {
        webSocketClient?.close()
        webSocketClient = null
    }
    
    fun sendMessage(data: Map<String, Any>) {
        try {
            val json = gson.toJson(data)
            webSocketClient?.send(json)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to send message", e)
        }
    }
    
    private fun sendRegistrationMessage() {
        val data = mapOf(
            "type" to "child_device_register",
            "deviceId" to preferenceManager.getDeviceId(),
            "childName" to preferenceManager.getChildName(),
            "parentCode" to preferenceManager.getParentCode(),
            "deviceInfo" to getDeviceInfo()
        )
        sendMessage(data)
    }
    
    private fun handleMessage(message: String?) {
        if (message == null) return
        
        try {
            val data = gson.fromJson(message, Map::class.java)
            val command = data["command"] as? String ?: return
            
            when (command) {
                "get_location" -> sendLocationUpdate()
                "get_app_usage" -> sendAppUsageUpdate()
                "block_app" -> blockApp(data["packageName"] as? String)
                "take_screenshot" -> takeScreenshot()
                else -> Log.d(TAG, "Unknown command: $command")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to handle message", e)
        }
    }
    
    private fun getDeviceInfo(): Map<String, String> {
        return mapOf(
            "model" to android.os.Build.MODEL,
            "manufacturer" to android.os.Build.MANUFACTURER,
            "androidVersion" to android.os.Build.VERSION.RELEASE
        )
    }
    
    private fun sendLocationUpdate() {
        // Implement location tracking
    }
    
    private fun sendAppUsageUpdate() {
        // Implement app usage tracking
    }
    
    private fun blockApp(packageName: String?) {
        // Implement app blocking
    }
    
    private fun takeScreenshot() {
        // Implement screenshot capture
    }
    
    private fun reconnect() {
        // Implement reconnection logic
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            connect()
        }, 5000)
    }
}
