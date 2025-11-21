package com.childsafety.parent.data

data class ChildDevice(
    val id: String,
    val name: String,
    val isOnline: Boolean,
    val location: String,
    val screenTime: String,
    val lastUpdate: Long,
    val deviceInfo: DeviceInfo? = null
)

data class DeviceInfo(
    val model: String,
    val manufacturer: String,
    val androidVersion: String,
    val battery: Int
)

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Double,
    val timestamp: Long
)

data class AppUsage(
    val packageName: String,
    val appName: String,
    val usageTime: Long,
    val lastUsed: Long
)

data class SafetyAlert(
    val id: String,
    val type: AlertType,
    val message: String,
    val timestamp: Long,
    val childId: String,
    val isRead: Boolean = false
)

enum class AlertType {
    GEOFENCE_EXIT,
    INAPPROPRIATE_CONTENT,
    EXCESSIVE_SCREEN_TIME,
    SUSPICIOUS_APP,
    DEVICE_OFFLINE,
    SOS_ALERT
}
