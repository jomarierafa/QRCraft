package com.jvrcoding.qrcraft.app.di

import com.jvrcoding.qrcraft.qr.data.scanner.QrScannerRepositoryImpl
import com.jvrcoding.qrcraft.qr.domain.scanner.QrScannerRepository
import com.jvrcoding.qrcraft.qr.presentation.qr_scanner.QRScannerViewModel
import com.jvrcoding.qrcraft.qr.presentation.scan_result.ScanResultViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
val appModule = module {
    viewModelOf(::QRScannerViewModel)
    viewModelOf(::ScanResultViewModel)

    singleOf(::QrScannerRepositoryImpl).bind<QrScannerRepository>()
}