package com.jvrcoding.qrcraft.qr.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jvrcoding.qrcraft.qr.data.database.dao.QrDao
import com.jvrcoding.qrcraft.qr.data.database.entity.QrEntity

@Database(
    entities = [
        QrEntity::class,
    ],
    version = 1
)
abstract class QrDatabase: RoomDatabase() {

    abstract val qrDao: QrDao
}