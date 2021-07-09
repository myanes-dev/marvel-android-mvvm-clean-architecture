package dev.myanes.marvelheroes

import android.app.Application
import dev.myanes.marvelheroes.config.getDIModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                getDIModules()
            )
        }

    }
}