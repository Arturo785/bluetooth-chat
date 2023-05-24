package com.example.bluetoothapp.data.chat

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

// our broadcast receiver to listen every action the phone does
class FoundDeviceReceiver(
    private val onDeviceFound: (BluetoothDevice) -> Unit // our lambda
) : BroadcastReceiver() {


    // will listen to every  BluetoothDevice.ACTION_FOUND in the whole phone
    // starts working for this
    //   context.registerReceiver(
    //            foundDeviceReceiver,
    //            IntentFilter(BluetoothDevice.ACTION_FOUND) // the intent to be found and caught
    //        )

    // the action to perform when an intent action is received
    override fun onReceive(context: Context?, intent: Intent?) {
        // we filter for the ones we care about only
        when (intent?.action) {
            BluetoothDevice.ACTION_FOUND -> {
                // depending on the current version of android we extract the data from the bluetooth
                // at the end we receive a bluetooth device
                val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(
                        BluetoothDevice.EXTRA_DEVICE,
                        BluetoothDevice::class.java
                    )
                } else {
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                }

                // performs our callback with the device found
                device?.let {
                    onDeviceFound.invoke(it)
                }
            }
        }
    }

}