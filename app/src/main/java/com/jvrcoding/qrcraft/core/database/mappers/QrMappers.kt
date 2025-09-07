package com.jvrcoding.qrcraft.core.database.mappers

import com.jvrcoding.qrcraft.core.database.entity.QrEntity
import com.jvrcoding.qrcraft.qr.domain.qr.QrDetail
import java.time.Instant
import java.time.ZoneId
import java.util.UUID

fun QrEntity.toQrDetail(): QrDetail {
    return QrDetail(
        id = id,
        qrValue = qrValue,
        qrRawValue = qrRawValue,
        qrType = qrType,
        createdAt = Instant.parse(createdAt)
            .atZone(ZoneId.of("UTC"))
    )
}

fun QrDetail.toQrEntity(): QrEntity {
    return QrEntity(
        id = id ?: UUID.randomUUID().toString(),
        qrValue = qrValue,
        qrRawValue = qrRawValue,
        qrType = qrType,
        createdAt = createdAt.toInstant().toString()
    )
}