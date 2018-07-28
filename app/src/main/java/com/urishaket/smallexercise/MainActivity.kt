package com.urishaket.smallexercise

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import android.support.v7.widget.DividerItemDecoration
import org.jetbrains.anko.linearLayout


class MainActivity : AppCompatActivity() {
    private var adapter: BluetoothAdapter? = null
    private lateinit var paired_devices: Set<BluetoothDevice>
    private val REQUEST_ENABLE_BLUETOOTH = 1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter = BluetoothAdapter.getDefaultAdapter()
        if (adapter == null) {
            return
        }
        if (!adapter!!.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
            header.text = getString(R.string.enable)
        }
        val dividerItemDecoration = DividerItemDecoration(paired_device_list.getContext(),
                LinearLayoutManager.VERTICAL)
        paired_device_list.addItemDecoration(dividerItemDecoration)

        paired_device_list.layoutManager = LinearLayoutManager(this)
        pairedDeviceList()
    }

    private fun pairedDeviceList() {
        checkBTPermissions()
        paired_devices = adapter!!.bondedDevices
        val list: ArrayList<String> = ArrayList()
        val list2: ArrayList<String> = ArrayList()
        if (!paired_devices.isEmpty()) {
            for (device: BluetoothDevice in paired_devices) {
                list.add(device.name)
                list2.add(device.uuids.toString())
                Log.i("device", "" + device)
            }
        } else {
            header.text = getString(R.string.no_result)
        }

        paired_device_list.adapter = DevicesRecycleViewAdapter(list, list2, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                if (adapter!!.isEnabled) {
                    toast("Bluetooth has been enabled")
                } else {
                    toast("Bluetooth has been disabled")
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                toast("Bluetooth enabling has been canceled")
            }
        }
    }

    fun checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            var permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 101); //Any number
            }
        }
    }
}