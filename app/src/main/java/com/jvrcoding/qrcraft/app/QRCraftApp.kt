package com.jvrcoding.qrcraft.app

import android.app.Application
import com.jvrcoding.qrcraft.app.di.appModule
import org.koin.core.context.startKoin

class QRCraftApp: Application()  {

    override fun onCreate() {

        super.onCreate()
        startKoin {
            modules(
                appModule
            )
        }
    }

}