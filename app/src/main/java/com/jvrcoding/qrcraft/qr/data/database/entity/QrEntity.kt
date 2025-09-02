package com.jvrcoding.qrcraft.qr.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jvrcoding.qrcraft.qr.domain.qr.QrType
import java.util.UUID

@Entity
data class QrEntity(
    val qrValue: String,
    val qrRawValue: String,
    val qrType: QrType,
    val createdAt: String,
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString()
)