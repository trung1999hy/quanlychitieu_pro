package com.example.quanlychitieu.ui

import android.app.Application
import android.provider.Settings
import com.example.quanlychitieu.local.Preference

class MainApp : Application() {
    var preference: Preference? = null
    override fun onCreate() {
        super.onCreate()
        instance = this
        preference = Preference.buildInstance(this)
    }

    companion object {
        private var instance: MainApp? = null
        @JvmStatic
        fun newInstance(): MainApp? {
            if (instance == null) {
                instance = MainApp()
            }
            return instance
        }
    }
    val deviceId: String
        get() = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
}