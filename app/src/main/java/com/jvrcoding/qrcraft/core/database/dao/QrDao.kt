package com.jvrcoding.qrcraft.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.jvrcoding.qrcraft.core.database.entity.QrEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QrDao {

    @Upsert
    suspend fun upsertQr(note: QrEntity)

    @Query("UPDATE qrentity SET qrTitleText = :text WHERE id = :id")
    suspend fun updateQrTitle(id: String, text: String)

    @Query("UPDATE qrentity SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean)

    @Query("SELECT * FROM qrentity WHERE id = :id")
    suspend fun getQr(id: String): QrEntity

    @Query("SELECT * FROM qrentity ORDER BY createdAt DESC")
    fun getQrList(): Flow<List<QrEntity>>

    @Query("DELETE FROM qrentity WHERE id=:id")
    suspend fun deleteQr(id: String)

    @Query("DELETE FROM QrEntity")
    suspend fun deleteAllQr()

}