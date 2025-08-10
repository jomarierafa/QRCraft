package com.jvrcoding.qrcraft.app.di

import com.jvrcoding.qrcraft.qr.presentation.qr_scanner.QRScannerViewModel
import com.jvrcoding.qrcraft.qr.presentation.scan_result.ScanResultViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::QRScannerViewModel)
    viewModelOf(::ScanResultViewModel)
}