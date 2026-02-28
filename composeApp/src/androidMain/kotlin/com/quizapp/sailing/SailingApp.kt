package com.quizapp.sailing

import android.app.Application
import com.quizapp.sailing.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SailingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SailingApp)
            modules(appModule)
        }
    }
}
