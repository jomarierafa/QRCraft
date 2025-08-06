package com.jvrcoding.qrcraft.di

import com.jvrcoding.qrcraft.qr_scanner.QRScannerViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::QRScannerViewModel)
}