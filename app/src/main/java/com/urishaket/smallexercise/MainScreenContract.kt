package com.urishaket.smallexercise

import android.app.Activity
import android.bluetooth.BluetoothDevice

interface MainScreenContract {

    interface View {
        fun setButtonText(state: Int)
        fun setHeaderText(state: Int)
        fun setRecyclerAdapter(listOFDevices: ArrayList<BluetoothDevice>)
        fun isDeviceListEmpty ():Boolean
        fun getFirstDeviceFromList ():BluetoothDevice
        fun updateNearbyList(dev : BluetoothDevice)
    }

    interface Presenter {
        fun scanForBT()
        fun isAdapterEnabled ():Boolean
        fun init(activity: Activity)
        fun getPairedDeviceList():Set<BluetoothDevice>
    }
}