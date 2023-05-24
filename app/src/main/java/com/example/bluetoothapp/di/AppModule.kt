package com.example.bluetoothapp.di

import android.content.Context
import com.example.bluetoothapp.data.chat.AndroidBluetoothController
import com.example.bluetoothapp.domain.chat.BluetoothController
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideBluetoothController(bluetoothController: AndroidBluetoothController): BluetoothController
}