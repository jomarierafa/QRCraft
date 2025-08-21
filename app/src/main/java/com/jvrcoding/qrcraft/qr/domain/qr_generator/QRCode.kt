package com.jvrcoding.qrcraft.qr.domain.qr_generator

data class QRCode(
    val width: Int,
    val height: Int,
    val pixels: Array<BooleanArray>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QRCode

        if (width != other.width) return false
        if (height != other.height) return false
        if (!pixels.contentDeepEquals(other.pixels)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        result = 31 * result + pixels.contentDeepHashCode()
        return result
    }
}
