package com.jvrcoding.qrcraft.qr.data.image

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.jvrcoding.qrcraft.qr.domain.image.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ImageRepoImpl(
    private val context: Context
): ImageRepository {

    override suspend fun saveImageToDownloads(
        bitmap: Bitmap,
        filename: String
    ): Uri? = withContext(Dispatchers.IO) {
        val resolver = context.contentResolver
        val mimeType = "image/jpeg"

        return@withContext if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, "$filename.jpg")
                put(MediaStore.Downloads.MIME_TYPE, mimeType)
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                put(MediaStore.Downloads.IS_PENDING, 1)
            }

            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)

            uri?.let {
                resolver.openOutputStream(it)?.use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                }
                values.clear()
                values.put(MediaStore.Downloads.IS_PENDING, 0)
                resolver.update(it, values, null, null)
            }

            uri
        } else {
            val downloadsDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()) downloadsDir.mkdirs()

            val file = File(downloadsDir, "$filename.jpg")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }

            MediaScannerConnection.scanFile(
                context,
                arrayOf(file.absolutePath),
                arrayOf(mimeType),
                null
            )

            Uri.fromFile(file)
        }
    }


}