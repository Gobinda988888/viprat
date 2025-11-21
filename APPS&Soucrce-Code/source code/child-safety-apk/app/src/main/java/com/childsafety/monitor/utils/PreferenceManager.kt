package com.childsafety.monitor.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "child_safety_prefs",
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val KEY_DEVICE_ID = "device_id"
        private const val KEY_SERVER_URL = "server_url"
        private const val KEY_PARENT_CODE = "parent_code"
        private const val KEY_CHILD_NAME = "child_name"
        private const val KEY_SETUP_COMPLETED = "setup_completed"
        private const val KEY_MONITORING_ENABLED = "monitoring_enabled"
    }
    
    fun getDeviceId(): String = prefs.getString(KEY_DEVICE_ID, "") ?: ""
    fun setDeviceId(id: String) = prefs.edit().putString(KEY_DEVICE_ID, id).apply()
    
    fun getServerUrl(): String = prefs.getString(KEY_SERVER_URL, "") ?: ""
    fun setServerUrl(url: String) = prefs.edit().putString(KEY_SERVER_URL, url).apply()
    
    fun getParentCode(): String = prefs.getString(KEY_PARENT_CODE, "") ?: ""
    fun setParentCode(code: String) = prefs.edit().putString(KEY_PARENT_CODE, code).apply()
    
    fun getChildName(): String = prefs.getString(KEY_CHILD_NAME, "") ?: ""
    fun setChildName(name: String) = prefs.edit().putString(KEY_CHILD_NAME, name).apply()
    
    fun isSetupCompleted(): Boolean = prefs.getBoolean(KEY_SETUP_COMPLETED, false)
    fun setSetupCompleted(completed: Boolean) = 
        prefs.edit().putBoolean(KEY_SETUP_COMPLETED, completed).apply()
    
    fun isMonitoringEnabled(): Boolean = prefs.getBoolean(KEY_MONITORING_ENABLED, false)
    fun setMonitoringEnabled(enabled: Boolean) = 
        prefs.edit().putBoolean(KEY_MONITORING_ENABLED, enabled).apply()
}
