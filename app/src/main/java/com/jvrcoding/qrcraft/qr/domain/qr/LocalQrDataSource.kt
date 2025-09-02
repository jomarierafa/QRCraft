package com.jvrcoding.qrcraft.qr.domain.qr

import com.jvrcoding.qrcraft.core.domain.DataError
import com.jvrcoding.qrcraft.core.domain.Result
import kotlinx.coroutines.flow.Flow

typealias QrDetailId = String

interface LocalQrDataSource {
    suspend fun getQr(id: QrDetailId): QrDetail?
    fun getQrList(): Flow<List<QrDetail>>
    suspend fun upsertQr(note: QrDetail): Result<QrDetailId, DataError.Local>
    suspend fun upsertQrList(notes: List<QrDetail>): Result<List<QrDetailId>, DataError.Local>
    suspend fun deleteQr(id: QrDetailId)
    suspend fun deleteAllQr()
}