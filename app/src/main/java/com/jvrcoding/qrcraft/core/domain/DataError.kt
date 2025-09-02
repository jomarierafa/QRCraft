package com.jvrcoding.qrcraft.core.domain

sealed interface DataError: Error {

    enum class Local: DataError {
        DISK_FULL
    }
}