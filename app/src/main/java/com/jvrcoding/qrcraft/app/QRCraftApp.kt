package com.jvrcoding.qrcraft.app

import android.app.Application
import com.jvrcoding.qrcraft.app.di.appModule
import com.jvrcoding.qrcraft.core.database.di.databaseModule
import com.jvrcoding.qrcraft.qr.di.qrModule
import org.koin.core.context.startKoin

class QRCraftApp: Application()  {

    override fun onCreate() {

        super.onCreate()
        startKoin {
            modules(
                appModule,
                qrModule,
                databaseModule
            )
        }
    }

}