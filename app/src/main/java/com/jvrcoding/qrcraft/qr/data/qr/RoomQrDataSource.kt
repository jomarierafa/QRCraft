package com.jvrcoding.qrcraft.qr.data.qr

import com.jvrcoding.qrcraft.core.database.dao.QrDao
import com.jvrcoding.qrcraft.core.database.mappers.toQrDetail
import com.jvrcoding.qrcraft.core.database.mappers.toQrEntity
import com.jvrcoding.qrcraft.qr.domain.qr.LocalQrDataSource
import com.jvrcoding.qrcraft.qr.domain.qr.QrDetail
import com.jvrcoding.qrcraft.qr.domain.qr.QrDetailId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomQrDataSource(
    private val qrDao: QrDao
): LocalQrDataSource {
    override suspend fun getQr(id: QrDetailId): QrDetail? {
        return qrDao.getQr(id).toQrDetail()
    }

    override fun getQrList(): Flow<List<QrDetail>> {
        return qrDao.getQrList()
            .map { noteEntities ->
                noteEntities.map { it.toQrDetail() }
            }
    }

    override suspend fun upsertQr(qr: QrDetail): QrDetailId {
        val entity = qr.toQrEntity()
        qrDao.upsertQr(entity)
        return entity.id
    }

    override suspend fun updateQrTitle(id: QrDetailId, text: String) {
        qrDao.updateQrTitle(
            id = id,
            text = text
        )
    }

    override suspend fun updateQrFavoriteStatus(
        id: QrDetailId,
        isFavorite: Boolean
    ) {
        qrDao.updateFavoriteStatus(
            id = id,
            isFavorite = isFavorite
        )
    }

    override suspend fun deleteQr(id: QrDetailId) {
        qrDao.deleteQr(id)
    }

    override suspend fun deleteAllQr() {
        qrDao.deleteAllQr()
    }

}