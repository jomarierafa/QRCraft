package com.jvrcoding.qrcraft.core.database.di


import androidx.room.Room
import com.jvrcoding.qrcraft.core.database.QrDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidApplication(),
            QrDatabase::class.java,
            "qr.db"
        ).build()
    }
    single { get<QrDatabase>().qrDao }
}