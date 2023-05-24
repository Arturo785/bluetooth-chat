package com.example.bluetoothapp.presentation

import com.example.bluetoothapp.domain.chat.BluetoothDeviceDomain


data class BluetoothUiState(
    val scannedDevices: List<BluetoothDeviceDomain> = emptyList(),
    val pairedDevices: List<BluetoothDeviceDomain> = emptyList(),
)