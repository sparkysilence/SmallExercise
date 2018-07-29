package com.urishaket.smallexercise

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.fragment_main_screen.*
import org.jetbrains.anko.support.v4.toast

class MainScreenFragment() : android.support.v4.app.Fragment(), MainScreenContract.View {
    val SCAN_BUTTON_TEXT_NO_RESULT = 0

    val SCAN_BUTTON_TEXT_RESULT = 1

    val HEADER_TEXT_NO_RESULT = 0

    val HEADER_TEXT_ENABLE = 1

    val SCAN_BUTTON_TEXT_SCAN = 2

    private val TAG = "MainFragment"

    private val REQUEST_ENABLE_BLUETOOTH = 1

    private val arrayOfFoundBTDevices: ArrayList<BluetoothDevice> = ArrayList()

    private lateinit var mDevicesRecycleViewAdapter: DevicesRecycleViewAdapter

    private lateinit var mPairedDevicesRecycleViewAdapter: DevicesRecycleViewAdapter

    var presenter: MainScreenContract.Presenter = MainScreenFragmentPresenter()

    companion object {
        fun newInstance(): MainScreenFragment {
            return MainScreenFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_main_screen, container, false)
    }

    override fun onStart() {
        super.onStart()

        scan_button.setOnClickListener() {
            scanOnclick()
        }

        presenter.init(getActivity()!!)

        setDecorationForRecycleViews()

        pairedDeviceList()

        registerBroadcastReciver()

        mDevicesRecycleViewAdapter = DevicesRecycleViewAdapter(arrayOfFoundBTDevices, getActivity()!!)
        device_list.adapter = mDevicesRecycleViewAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                if (presenter.isAdapterEnabled()) {
                    toast(R.string.bluetooth_enabled)
                } else {
                    toast(R.string.bluetooth_disabled)
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                toast(R.string.bluetooth_canceled)
            }
        }
    }

    override fun setButtonText(state: Int) {
        when (state) {
            SCAN_BUTTON_TEXT_NO_RESULT -> scan_button.text = getString(R.string.no_result)
            SCAN_BUTTON_TEXT_RESULT -> scan_button.text = getString(R.string.result)
            SCAN_BUTTON_TEXT_SCAN -> scan_button.text = getString(R.string.scan)
        }
    }

    override fun setHeaderText(state: Int) {
        when (state) {
            HEADER_TEXT_NO_RESULT -> header.text = getString(R.string.paired_devices_no_result)
            HEADER_TEXT_ENABLE -> header.text = getString(R.string.enable)
        }
    }

    override fun setRecyclerAdapter(listOFDevices: ArrayList<BluetoothDevice>) {
        mPairedDevicesRecycleViewAdapter = DevicesRecycleViewAdapter(listOFDevices, getActivity()!!)
        paired_device_list.adapter = mPairedDevicesRecycleViewAdapter
    }

    override fun isDeviceListEmpty(): Boolean {
        return arrayOfFoundBTDevices.isEmpty()
    }

    override fun getFirstDeviceFromList(): BluetoothDevice {
        return arrayOfFoundBTDevices.first();
    }

    override fun updateNearbyList(dev: BluetoothDevice) {
        var remove = ArrayList<BluetoothDevice>()
        if (mDevicesRecycleViewAdapter.devices.size > 0) {
            for (i in mDevicesRecycleViewAdapter.devices) {
                if (i.address == dev.address) {
                    remove.add(i)
                    Log.i(TAG, "removed device from recycle view")
                }
            }
            mDevicesRecycleViewAdapter.devices.removeAll(remove)
        }
        mDevicesRecycleViewAdapter.add(dev)
        Log.i(TAG, "added device from recycle view")
    }

    fun pairedDeviceList() {
        var paired_devices = presenter.getPairedDeviceList()
        if (!paired_devices.isEmpty()) {
            val listOFDevices: ArrayList<BluetoothDevice> = ArrayList()
            for (device: BluetoothDevice in paired_devices) {
                listOFDevices.add(device)
            }
            setRecyclerAdapter(listOFDevices)
        } else {
            setHeaderText(HEADER_TEXT_NO_RESULT)
        }
    }

    fun setDecorationForRecycleViews() {
        val dividerItemDecoration = DividerItemDecoration(paired_device_list.getContext(),
                LinearLayoutManager.VERTICAL)
        paired_device_list.addItemDecoration(dividerItemDecoration)
        paired_device_list.layoutManager = LinearLayoutManager(getActivity())
        device_list.addItemDecoration(dividerItemDecoration)
        device_list.layoutManager = LinearLayoutManager(getActivity())
    }

    fun registerBroadcastReciver() {
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        filter.addAction(BluetoothDevice.ACTION_UUID)
        val mReceiver = SearchDeviceBroadcastReciver(this)
        getActivity()!!.registerReceiver(mReceiver, filter)
    }

    fun scanOnclick() {
        mDevicesRecycleViewAdapter.clear()
        presenter.scanForBT()
    }
}