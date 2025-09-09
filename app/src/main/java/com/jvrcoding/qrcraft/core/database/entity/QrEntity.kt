package com.jvrcoding.qrcraft.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jvrcoding.qrcraft.qr.domain.qr.QrType
import com.jvrcoding.qrcraft.qr.domain.qr.Transaction
import java.util.UUID

@Entity
data class QrEntity(
    val qrValue: String,
    val qrRawValue: String,
    val qrTitleText: String,
    val qrType: QrType,
    val transactionType: Transaction,
    val createdAt: String,
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString()
)