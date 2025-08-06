package com.jvrcoding.qrcraft

import android.app.Application
import com.jvrcoding.qrcraft.di.appModule
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