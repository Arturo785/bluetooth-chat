package com.example.bluetoothapp.data.chat

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import com.example.bluetoothapp.domain.chat.BluetoothController
import com.example.bluetoothapp.domain.chat.BluetoothDeviceDomain
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@SuppressLint("MissingPermission")
class AndroidBluetoothController @Inject constructor(
    @ApplicationContext private val context: Context
) : BluetoothController {


    // we retrieve the managers for our bluetooth from the system
    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }

    // we extract the adapter
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }


    // privates, the ones that can set data
    // keeps the single source of data and only this class should be able to modify and add data
    private val _scannedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    private val _pairedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())

    // the ones that can be accessed but not set data, meant to be listened
    override val scannedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _scannedDevices.asStateFlow()

    override val pairedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _pairedDevices.asStateFlow()

    ///**asStateFlow()
    // * Represents this mutable state flow as a read-only state flow.


    // the instance of our broadcast receiver
    // we use our broadcast receiver callback we did

    // starts working once gets registered in the context //context.registerReceiver(
    private val foundDeviceReceiver = FoundDeviceReceiver { deviceFound ->
        // with .update we also receive the current value saved and we can either replace it or play with it
        _scannedDevices.update { devices ->
            val newDevice = deviceFound.toBluetoothDeviceDomain()
            // if already in the list update with same list otherwise the list + our new one
            if (newDevice in devices) devices else devices + newDevice
        }
    }// has to be released in our onReleased fun to avoid leaks


    // remember init goes here to allow the upper values to be instanced
    // kotlin follows the order of appearance
    // first the normal constructor, then the list and flows, then this init
    init {
        updatePairedDevices()
    }


    override fun startDiscovery() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
            return // no permission, then returns
        }

        // foundDeviceReceiver is a broadcastReceiver needed to catch when a new device is available
        // we register in this part to start operating and receive results

        // the register of broadcastReceivers consists in the broadcastReceiver instance and the type of intents it will catch
        // this triggers the broadcastReceiver to start working
        context.registerReceiver(
            foundDeviceReceiver,
            IntentFilter(BluetoothDevice.ACTION_FOUND)
        )

        updatePairedDevices()

        // enables the system to search for bluetooth devices, when a new fount by the broadcastReceiver the callback triggers
        bluetoothAdapter?.startDiscovery()
    }

    override fun stopDiscovery() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
            return // no permission do nothing
        }

        // this comes from the system
        bluetoothAdapter?.cancelDiscovery()
    }

    override fun release() {
        // unregisters our context receiver
        context.unregisterReceiver(foundDeviceReceiver)
    }

    // retrieves the list of previous devices
    private fun updatePairedDevices() {

        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            return // no permission, show nothing
        }

        // gets the saved and bonded ones and updates the flow with our own datatype we did
        bluetoothAdapter
            ?.bondedDevices
            ?.map { it.toBluetoothDeviceDomain() }
            ?.also { devices ->
                _pairedDevices.update { devices }
            }
        //Updates the MutableStateFlow.value atomically using the specified function of its value.
    }


    // the permissions are in the manifest also
    private fun hasPermission(permission: String): Boolean {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

}