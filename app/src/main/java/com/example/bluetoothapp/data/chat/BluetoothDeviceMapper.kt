package com.example.bluetoothapp.data.chat

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.example.bluetoothapp.domain.chat.BluetoothDeviceDomain

// maps the class from the system to the one we did
@SuppressLint("MissingPermission") // the ext function is always used after checking permission
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain {
    return BluetoothDeviceDomain(
        name = name,
        address = address
    )
}