package com.jvrcoding.qrcraft.qr.domain.qr

import java.time.ZonedDateTime

data class QrDetail(
    val id: String,
    val qrValue: String,
    val qrRawValue: String,
    val qrType: QrType,
    val transactionType: Transaction,
    val createdAt: ZonedDateTime,
)

enum class Transaction {
    SCANNED, GENERATED
}

