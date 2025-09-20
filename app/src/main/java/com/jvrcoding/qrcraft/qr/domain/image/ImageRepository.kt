package com.jvrcoding.qrcraft.qr.domain.image

import android.graphics.Bitmap
import android.net.Uri

interface ImageRepository {

    suspend fun saveImageToDownloads(bitmap: Bitmap, filename: String): Uri?
}