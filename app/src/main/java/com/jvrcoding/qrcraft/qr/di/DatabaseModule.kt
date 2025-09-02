package com.jvrcoding.qrcraft.qr.di


import androidx.room.Room
import com.jvrcoding.qrcraft.qr.data.database.QrDatabase
import com.jvrcoding.qrcraft.qr.data.database.RoomLocalQrDataSource
import com.jvrcoding.qrcraft.qr.domain.qr.LocalQrDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
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

    singleOf(::RoomLocalQrDataSource).bind<LocalQrDataSource>()
}