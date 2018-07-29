package com.urishaket.smallexercise

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class SearchDeviceBroadcastReciver(var mainFragment:MainScreenContract.View,var presenter: MainScreenContract.Presenter):BroadcastReceiver(){
    private val TAG = "BroadcastReciver"
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
                var device = mainFragment.getFirstDeviceFromList();
                device.fetchUuidsWithSdp();
                mainFragment.setButtonText(2)
            }else{
                mainFragment.setButtonText(0)
            }
            Log.i(TAG,"Discovery has finished")
            presenter.scanForBT()
        } else if (BluetoothDevice.ACTION_UUID.equals(action)) {
            var deviceExtra = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE);
            var uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
            Log.i(TAG,"DeviceExtra address - " + deviceExtra.getAddress());
            if (uuidExtra != null) {
                mainFragment.updateNearbyList(deviceExtra)
            } else {
                Log.i(TAG,"uuidExtra is still null");
            }
        }
    }
}

