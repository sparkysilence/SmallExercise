package com.urishaket.smallexercise

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat

class MainScreenFragmentPresenter:MainScreenContract.Presenter{
    private var adapter: BluetoothAdapter? = null
    private lateinit var view: MainScreenContract.View
    private val REQUEST_ENABLE_BLUETOOTH = 1

    override  fun init(activity: Activity){
        checkBlueToothAdapter(activity)
        checkBTPermissions(activity)
        scanForBT()
    }

    override fun scanForBT() {
        adapter!!.startDiscovery()
    }

    override fun isAdapterEnabled(): Boolean {
        return adapter!!.isEnabled
    }

    override fun getPairedDeviceList():Set<BluetoothDevice>{
        return adapter!!.bondedDevices
    }

    fun checkBlueToothAdapter(activity: Activity) {
        adapter = BluetoothAdapter.getDefaultAdapter()
        if (adapter == null) {
            return
        }
        if (!adapter!!.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
            view.setHeaderText(1)
        }
    }

    fun checkBTPermissions(activity: Activity) {
        var permissionCheck = activity.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
        permissionCheck += activity.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
        if (permissionCheck != 0) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 101); //Any number
        }
    }
}