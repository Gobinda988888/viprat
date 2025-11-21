package com.childsafety.parent.utils

import android.content.Context
import android.content.SharedPreferences
import kotlin.random.Random

class PreferenceManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "parent_control_prefs",
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val KEY_PARENT_ID = "parent_id"
        private const val KEY_SERVER_URL = "server_url"
        private const val KEY_LINKING_CODE = "linking_code"
        private const val KEY_SETUP_COMPLETED = "setup_completed"
    }
    
    fun getParentId(): String {
        var id = prefs.getString(KEY_PARENT_ID, null)
        if (id == null) {
            id = generateParentId()
            setParentId(id)
        }
        return id
    }
    
    fun setParentId(id: String) = prefs.edit().putString(KEY_PARENT_ID, id).apply()
    
    fun getServerUrl(): String = prefs.getString(KEY_SERVER_URL, "") ?: ""
    fun setServerUrl(url: String) = prefs.edit().putString(KEY_SERVER_URL, url).apply()
    
    fun getLinkingCode(): String {
        var code = prefs.getString(KEY_LINKING_CODE, null)
        if (code == null) {
            code = generateLinkingCode()
            setLinkingCode(code)
        }
        return code
    }
    
    fun setLinkingCode(code: String) = prefs.edit().putString(KEY_LINKING_CODE, code).apply()
    
    fun isSetupCompleted(): Boolean = prefs.getBoolean(KEY_SETUP_COMPLETED, false)
    fun setSetupCompleted(completed: Boolean) = 
        prefs.edit().putBoolean(KEY_SETUP_COMPLETED, completed).apply()
    
    private fun generateParentId(): String {
        return "P${System.currentTimeMillis().toString().takeLast(8)}"
    }
    
    private fun generateLinkingCode(): String {
        return Random.nextInt(100000, 999999).toString()
    }
}
