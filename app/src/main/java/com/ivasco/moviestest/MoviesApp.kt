package com.ivasco.moviestest

import android.app.Application
import com.ivasco.moviestest.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
class MoviesApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Inject Android Context
            androidContext(this@MoviesApp)

            // Modules here
            modules(
                listOf(
                    appModule
                )
            )
        }
    }
}