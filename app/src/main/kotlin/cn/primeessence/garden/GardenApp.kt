package cn.primeessence.garden

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GardenApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
