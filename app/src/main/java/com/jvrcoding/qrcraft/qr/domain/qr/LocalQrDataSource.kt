package com.jvrcoding.qrcraft.qr.domain.qr

import kotlinx.coroutines.flow.Flow

typealias QrDetailId = String

interface LocalQrDataSource {
    suspend fun getQr(id: QrDetailId): QrDetail?
    fun getQrList(): Flow<List<QrDetail>>
    suspend fun upsertQr(qr: QrDetail): QrDetailId
    suspend fun updateQrTitle(id: QrDetailId, text: String)
    suspend fun updateQrFavoriteStatus(id: QrDetailId, isFavorite: Boolean)
    suspend fun deleteQr(id: QrDetailId)
    suspend fun deleteAllQr()
}