package com.jvrcoding.qrcraft.qr.di

import com.jvrcoding.qrcraft.qr.data.scanner.QrScannerRepositoryImpl
import com.jvrcoding.qrcraft.qr.domain.scanner.QrScannerRepository
import com.jvrcoding.qrcraft.qr.presentation.data_entry.DataEntryViewModel
import com.jvrcoding.qrcraft.qr.presentation.main.MainViewModel
import com.jvrcoding.qrcraft.qr.presentation.qr_scanner.QRScannerViewModel
import com.jvrcoding.qrcraft.qr.presentation.scan_result.ScanResultViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module


@OptIn(ExperimentalCoroutinesApi::class)
val qrModule = module {
    viewModelOf(::QRScannerViewModel)
    viewModelOf(::ScanResultViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::DataEntryViewModel)

    singleOf(::QrScannerRepositoryImpl).bind<QrScannerRepository>()

}