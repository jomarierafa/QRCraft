package com.jvrcoding.qrcraft.qr.presentation.util

import com.jvrcoding.qrcraft.qr.domain.qr.QrDetail
import com.jvrcoding.qrcraft.qr.presentation.models.QrTypeUi
import com.jvrcoding.qrcraft.qr.presentation.models.QrUi
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun QrDetail.toQrUi(): QrUi {
    val dateTimeInLocalTime = createdAt
        .withZoneSameInstant(ZoneId.systemDefault())
    val formattedDateTime = DateTimeFormatter
        .ofPattern("dd MMM yyyy, HH:mm")
        .format(dateTimeInLocalTime)

    return QrUi(
        id = id,
        qrType = QrTypeUi.valueOf(qrType.name),
        content = qrValue,
        date = formattedDateTime,
    )

}