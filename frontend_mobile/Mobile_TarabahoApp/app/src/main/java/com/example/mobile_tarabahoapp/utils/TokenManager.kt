package com.example.mobile_tarabahoapp.utils

import android.content.Context
import com.example.mobile_tarabahoapp.App

object TokenManager {
    private const val PREFS_NAME = "auth_prefs"
    private const val TOKEN_KEY = "jwt_token"

    fun saveToken(token: String) {
        val prefs = App.instance.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getToken(): String? {
        val prefs = App.instance.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(TOKEN_KEY, null)
    }

    fun clearToken() {
        val prefs = App.instance.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(TOKEN_KEY).apply()
    }

    private const val WORKER_ID_KEY = "worker_id"

    fun saveWorkerId(id: Long) {
        val prefs = App.instance.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        prefs.edit().putLong("worker_id", id).apply()
    }

    fun getWorkerId(): Long {
        val prefs = App.instance.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        return prefs.getLong("worker_id", -1L)
    }

}