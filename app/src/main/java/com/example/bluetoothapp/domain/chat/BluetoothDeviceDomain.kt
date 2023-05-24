package com.example.bluetoothapp.domain.chat

// this is to avoid conflicts with the default android bluetooth classes
// we can refer to the class we created with this typealias
typealias BluetoothDeviceDomain = BluetoothDevice

// represents the devices to be scanned
data class BluetoothDevice(
    val name: String?,
    val address: String
)