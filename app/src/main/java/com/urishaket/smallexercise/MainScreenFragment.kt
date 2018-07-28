package com.urishaket.smallexercise

import android.Manifest
import android.app.Activity
import android.app.Fragment
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.fragment_main_screen.*
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast

class MainScreenFragment : android.support.v4.app.Fragment(){

    companion object {

        fun newInstance(): MainScreenFragment {
            return MainScreenFragment()
        }

        val instance = this
    }


private var adapter: BluetoothAdapter? = null
private lateinit var paired_devices: Set<BluetoothDevice>
private val REQUEST_ENABLE_BLUETOOTH = 1
val arrayOfFoundBTDevicesUUID: ArrayList<String> = ArrayList()
val arrayOfFoundBTDevices: ArrayList<String> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_main_screen,container, false)
    }
    override fun onStart() {
        super.onStart()
        scan_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                adapter!!.startDiscovery()
            }})
        val dividerItemDecoration = DividerItemDecoration(paired_device_list.getContext(),
                LinearLayoutManager.VERTICAL)
        paired_device_list.addItemDecoration(dividerItemDecoration)
        paired_device_list.layoutManager = LinearLayoutManager(getActivity())
        device_list.addItemDecoration(dividerItemDecoration)
        device_list.layoutManager = LinearLayoutManager(getActivity())
        checkBlueToothAdapter()
        pairedDeviceList()
        checkBTPermissions()
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        getActivity()!!.registerReceiver(mReceiver, filter)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                if (adapter!!.isEnabled) {
                    toast(R.string.bluetooth_enabled)
                } else {
                    toast(R.string.bluetooth_disabled)
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                toast(R.string.bluetooth_canceled)
            }
        }

    }

    fun checkBlueToothAdapter (){
        adapter = BluetoothAdapter.getDefaultAdapter()
        if (adapter == null) {
            return
        }
        if (!adapter!!.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
            header.text = getString(R.string.enable)
        }
    }

    fun checkBTPermissions() {
        var permissionCheck = getActivity()!!.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
        permissionCheck += getActivity()!!.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
        if (permissionCheck != 0) {
            ActivityCompat.requestPermissions(getActivity()!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 101); //Any number
        }
    }

    private fun pairedDeviceList() {

        paired_devices = adapter!!.bondedDevices
        val list_of_names: ArrayList<String> = ArrayList()
        val list_of_uuids: ArrayList<String> = ArrayList()
        if (!paired_devices.isEmpty()) {
            for (device: BluetoothDevice in paired_devices) {
                list_of_names.add(device.name)
                list_of_uuids.add(device.uuids.toString())
            }
        } else {
            header.text = getString(R.string.no_result)
        }
        paired_device_list.adapter = DevicesRecycleViewAdapter(list_of_names, list_of_uuids, getActivity()!!)
    }

    fun updateNearbyList(name: String, addr: String){
       arrayOfFoundBTDevices.add(name)
       arrayOfFoundBTDevicesUUID.add(addr)
       device_list.adapter = DevicesRecycleViewAdapter(arrayOfFoundBTDevices,arrayOfFoundBTDevicesUUID ,getActivity()!!)
    }
}