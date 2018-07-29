package com.urishaket.smallexercise

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class SearchDeviceBroadcastReciver(private var mainFragment:MainScreenContract.View):BroadcastReceiver(){
    private val TAG = "BroadcastReciver"

    private val DISCOVERY_FINISHED =  "Discovery has finished"

    private val DEVICE_EXTRA_ADDRESS =  "DeviceExtra address"

    private val UUID_NULL =  "uuidExtra is still null"

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (BluetoothDevice.ACTION_FOUND == action) {
            val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            if (device.name!=null){
                mainFragment.updateNearbyList(device)
                mainFragment.setButtonText(1)
            }
        }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            if (!mainFragment.isDeviceListEmpty()) {
                val device = mainFragment.getFirstDeviceFromList();
                device.fetchUuidsWithSdp();
                mainFragment.setButtonText(2)
            }else{
                mainFragment.setButtonText(0)
            }
            Log.i(TAG,DISCOVERY_FINISHED)
        } else if (BluetoothDevice.ACTION_UUID.equals(action)) {
            val deviceExtra = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE);
            val uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
            Log.i(TAG,DEVICE_EXTRA_ADDRESS + deviceExtra.getAddress());
            if (uuidExtra != null) {
                mainFragment.updateNearbyList(deviceExtra)
            } else {
                Log.i(TAG,UUID_NULL);
            }
        }
    }
}

