package com.prdcv.ehust

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EHustApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}