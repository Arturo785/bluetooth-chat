package com.example.bluetoothapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bluetoothapp.domain.chat.BluetoothController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class BluetoothViewModel @Inject constructor(
    private val bluetoothController: BluetoothController // inject the interface, dagger provides the impl
) : ViewModel() {

    // the private one
    private val _state = MutableStateFlow(BluetoothUiState())

    // when any of this 3 parameters changes the inner code in the lambda gets triggered,
    // helps us to maintain up to date information when multiple sources are needed

    // the parameters listened are stateFlows in the controller
    val state = combine(
        bluetoothController.scannedDevices,
        bluetoothController.pairedDevices,
        _state
    ) { scannedDevices, pairedDevices, state ->
        state.copy(
            scannedDevices = scannedDevices,
            pairedDevices = pairedDevices
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)
    //The stateIn operator in Kotlin Flows is used to transform a Flow into a StateFlow.
    /*by specifying an initial value and a CoroutineScope.
    The initial value represents the starting value of the StateFlow,
    and the CoroutineScope is used to control the lifespan of the StateFlow.*/

    // SharingStarted.WhileSubscribed
    // will be executed as long there is a subscriber
    // then the last subscribers stops listening will wait 5 more seconds, if no one resubscribes then gets cleaned
    // helps in case of rotations and change of lifecycle

    //The stateIn operator takes three parameters:
    //
    //The CoroutineScope (this) represents the scope in which the StateFlow will be created.
    //The SharingStarted.Eagerly parameter specifies the sharing mode for the StateFlow. In this case, it eagerly shares the StateFlow to all collectors.
    //The initial value 0 is the starting value of the StateFlow.

    fun startScan() {
        bluetoothController.startDiscovery()
    }

    fun stopScan() {
        bluetoothController.stopDiscovery()
    }
}