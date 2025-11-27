
package com.example.bloom_app

import android.app.Application
import com.example.bloom_app.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BloomApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@BloomApplication)
            modules(com.example.bloom_app.di.appModule)
        }
    }
}