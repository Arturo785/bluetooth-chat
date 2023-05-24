package com.example.bluetoothapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bluetoothapp.domain.chat.BluetoothDeviceDomain
import com.example.bluetoothapp.presentation.BluetoothUiState

@Composable
fun DeviceScreen(
    state: BluetoothUiState,
    onStartScan: () -> Unit,
    onStopScan: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BluetoothDeviceList(
            pairedDevices = state.pairedDevices,
            scannedDevices = state.scannedDevices,
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
            // with this weight pushes the buttons to the bottom
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = onStartScan) {
                Text(text = "Start scan")
            }
            Button(onClick = onStopScan) {
                Text(text = "Stop scan")
            }
        }
    }
}

@Composable
fun BluetoothDeviceList(
    pairedDevices: List<BluetoothDeviceDomain>,
    scannedDevices: List<BluetoothDeviceDomain>,
    onClick: (BluetoothDeviceDomain) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        // needs import androidx.compose.foundation.lazy.items

        // this item {} what it does is a section in the lazyColumns and allows to have multiple types of composables in the same lazyColumn
        item {
            Text(
                text = "Paired Devices block",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
        // the items under the section
        items(pairedDevices) { device ->
            Text(
                text = device.name ?: "(No name)",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick(device) }
                    .padding(16.dp)
            )
        }

        // the section
        item {
            Text(
                text = "Scanned Devices block",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
        // items under the section
        items(scannedDevices) { device ->
            Text(
                text = device.name ?: "(No name)",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick(device) }
                    .padding(16.dp)
            )
        }
    }
}

@Composable
@Preview
fun DeviceScreen() {

    val listOfBluetoothDeviceDomain = mutableListOf<BluetoothDeviceDomain>()
    val listOfBluetoothDeviceDomain2 = mutableListOf<BluetoothDeviceDomain>()
    for (i in 0 until 5) {
        listOfBluetoothDeviceDomain.add(BluetoothDeviceDomain("name $i", "address $i"))
        listOfBluetoothDeviceDomain2.add(BluetoothDeviceDomain("name2 $i", "address2 $i"))
    }
    val state = BluetoothUiState(
        listOfBluetoothDeviceDomain,
        listOfBluetoothDeviceDomain2
    )
    DeviceScreen(
        state = state,
        onStartScan = {},
        onStopScan = {}
    )
}