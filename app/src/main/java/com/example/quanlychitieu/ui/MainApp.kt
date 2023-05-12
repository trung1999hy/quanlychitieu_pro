package com.example.quanlychitieu.ui

import android.app.Application
import com.example.quanlychitieu.local.Preference

class MainApp : Application() {
    var preference: Preference? = null
    override fun onCreate() {
        super.onCreate()
        instance = this
        preference = Preference.buildInstance(this)
        if (preference?.firstInstall == false) {
            preference?.firstInstall = true
            preference?.setValueCoin(10)
        }

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
}