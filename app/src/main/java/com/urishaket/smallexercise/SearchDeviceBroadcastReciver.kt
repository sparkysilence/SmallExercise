package com.urishaket.smallexercise

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.urishaket.smallexercise.R.id.device_list
import kotlinx.android.synthetic.main.fragment_main_screen.*

class SearchDeviceBroadcastReciver:BroadcastReceiver(){


    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (BluetoothDevice.ACTION_FOUND == action) {
            val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            MainScreenFragment().updateNearbyList(device.name,device.address)
        }
    }
}
