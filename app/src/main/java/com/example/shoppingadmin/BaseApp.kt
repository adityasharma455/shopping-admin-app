    package com.example.shoppingadmin

    import android.app.Application
    import org.koin.android.ext.koin.androidContext
    import org.koin.core.context.startKoin

    class BaseApp : Application() {

        override fun onCreate() {
            super.onCreate()
            startKoin {
                androidContext(this@BaseApp)
                modules(CombinedappModules)
            }
        }

    }