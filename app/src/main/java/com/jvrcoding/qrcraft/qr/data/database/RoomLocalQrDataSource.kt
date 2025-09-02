package com.jvrcoding.qrcraft.qr.data.database

import android.database.sqlite.SQLiteFullException
import com.jvrcoding.qrcraft.core.domain.DataError
import com.jvrcoding.qrcraft.core.domain.Result
import com.jvrcoding.qrcraft.qr.data.database.dao.QrDao
import com.jvrcoding.qrcraft.qr.data.database.mappers.toQrDetail
import com.jvrcoding.qrcraft.qr.data.database.mappers.toQrEntity
import com.jvrcoding.qrcraft.qr.domain.qr.LocalQrDataSource
import com.jvrcoding.qrcraft.qr.domain.qr.QrDetailId
import com.jvrcoding.qrcraft.qr.domain.qr.QrDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomLocalQrDataSource(
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

    override suspend fun upsertQr(note: QrDetail): Result<QrDetailId, DataError.Local> {
        return try {
            val entity = note.toQrEntity()
            qrDao.upsertQr(entity)
            Result.Success(entity.id)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun upsertQrList(notes: List<QrDetail>): Result<List<QrDetailId>, DataError.Local> {
        return try {
            val entities = notes.map { it.toQrEntity() }
            qrDao.upsertQrList(entities)
            Result.Success(entities.map { it.id })
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteQr(id: QrDetailId) {
        qrDao.deleteQr(id)
    }

    override suspend fun deleteAllQr() {
        qrDao.deleteAllQr()
    }

}