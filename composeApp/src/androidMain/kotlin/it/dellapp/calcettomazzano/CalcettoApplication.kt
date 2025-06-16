package it.dellapp.calcettomazzano

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class CalcettoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@CalcettoApplication)
            androidLogger()
            modules()
        }
    }
}