package com.urishaket.smallexercise

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice

interface MainScreenInterface
{

    interface View {
        fun setButtonText(state: Int)
        fun setHeaderText(state: Int)
        fun setRecyclerAdapter(listOFDevices: ArrayList<BluetoothDevice>)
        fun isDeviceListEmpty ():Boolean
        fun getFirstDeviceFromList ():BluetoothDevice
        fun updateNearbyList(dev : BluetoothDevice)
    }

}