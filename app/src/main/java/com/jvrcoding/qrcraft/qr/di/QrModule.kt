package com.jvrcoding.qrcraft.qr.di

import com.jvrcoding.qrcraft.qr.data.qr.RoomQrDataSource
import com.jvrcoding.qrcraft.qr.data.qr_generator.ZxingQrCodeGenerator
import com.jvrcoding.qrcraft.qr.data.scanner.MLKitScanner
import com.jvrcoding.qrcraft.qr.domain.qr.LocalQrDataSource
import com.jvrcoding.qrcraft.qr.domain.qr_generator.QrCodeGenerator
import com.jvrcoding.qrcraft.qr.domain.scanner.QrScanner
import com.jvrcoding.qrcraft.qr.presentation.data_entry.DataEntryViewModel
import com.jvrcoding.qrcraft.qr.presentation.history.HistoryViewModel
import com.jvrcoding.qrcraft.qr.presentation.main.MainViewModel
import com.jvrcoding.qrcraft.qr.presentation.qr_scanner.QRScannerViewModel
import com.jvrcoding.qrcraft.qr.presentation.preview.PreviewViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module


@OptIn(ExperimentalCoroutinesApi::class)
val qrModule = module {
    viewModelOf(::QRScannerViewModel)
    viewModelOf(::PreviewViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::DataEntryViewModel)
    viewModelOf(::HistoryViewModel)

    singleOf(::MLKitScanner).bind<QrScanner>()
    singleOf(::ZxingQrCodeGenerator).bind<QrCodeGenerator>()
    singleOf(::RoomQrDataSource).bind<LocalQrDataSource>()

}