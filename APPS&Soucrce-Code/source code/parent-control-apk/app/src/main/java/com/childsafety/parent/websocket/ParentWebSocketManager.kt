package com.childsafety.parent.websocket

import android.content.Context
import android.util.Log
import com.childsafety.parent.utils.PreferenceManager
import com.google.gson.Gson
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class ParentWebSocketManager(private val context: Context) {
    
    private var webSocketClient: WebSocketClient? = null
    private val preferenceManager = PreferenceManager(context)
    private val gson = Gson()
    
    companion object {
        private const val TAG = "ParentWebSocket"
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
    
    fun sendCommand(deviceId: String, command: String, params: Map<String, Any>? = null) {
        val data = mutableMapOf(
            "type" to "parent_command",
            "parentId" to preferenceManager.getParentId(),
            "deviceId" to deviceId,
            "command" to command,
            "timestamp" to System.currentTimeMillis()
        )
        
        if (params != null) {
            data.putAll(params)
        }
        
        sendMessage(data)
    }
    
    private fun sendMessage(data: Map<String, Any>) {
        try {
            val json = gson.toJson(data)
            webSocketClient?.send(json)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to send message", e)
        }
    }
    
    private fun sendRegistrationMessage() {
        val data = mapOf(
            "type" to "parent_register",
            "parentId" to preferenceManager.getParentId(),
            "linkingCode" to preferenceManager.getLinkingCode()
        )
        sendMessage(data)
    }
    
    private fun handleMessage(message: String?) {
        if (message == null) return
        
        try {
            val data = gson.fromJson(message, Map::class.java)
            val type = data["type"] as? String ?: return
            
            when (type) {
                "child_device_connected" -> handleChildConnected(data)
                "child_device_update" -> handleChildUpdate(data)
                "child_device_disconnected" -> handleChildDisconnected(data)
                else -> Log.d(TAG, "Unknown message type: $type")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to handle message", e)
        }
    }
    
    private fun handleChildConnected(data: Map<*, *>) {
        Log.d(TAG, "Child device connected: $data")
        // Notify UI about new child device
    }
    
    private fun handleChildUpdate(data: Map<*, *>) {
        Log.d(TAG, "Child device update: $data")
        // Update child device info in UI
    }
    
    private fun handleChildDisconnected(data: Map<*, *>) {
        Log.d(TAG, "Child device disconnected: $data")
        // Update UI to show device offline
    }
    
    private fun reconnect() {
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            connect()
        }, 5000)
    }
}
