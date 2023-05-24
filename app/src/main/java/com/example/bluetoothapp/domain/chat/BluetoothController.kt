package com.example.bluetoothapp.domain.chat

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

// our contract to be implemented
interface BluetoothController {
    // this are the ones to be observed and consumed, the impl has private ones that set the data
    val isConnected: StateFlow<Boolean>
    val scannedDevices: StateFlow<List<BluetoothDeviceDomain>>
    val pairedDevices: StateFlow<List<BluetoothDeviceDomain>>
    val errors: SharedFlow<String>


    fun startDiscovery()
    fun stopDiscovery()

    // the datatype is flows because we need to be aware when new messages come in or any change that may happen
    fun startBluetoothServer(): Flow<ConnectionResult>
    fun connectToDevice(device: BluetoothDeviceDomain): Flow<ConnectionResult>

    fun closeConnection()
    fun release()
}