package com.urishaket.smallexercise

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.fragment_main_screen.*
import org.jetbrains.anko.support.v4.toast


class MainScreenFragment() : android.support.v4.app.Fragment(), MainScreenContract.View {
    private val TAG = "MainFragment"

    private val REQUEST_ENABLE_BLUETOOTH = 1

    private val arrayOfFoundBTDevices: ArrayList<BluetoothDevice> = ArrayList()

    var mDevicesRecycleViewAdapter: DevicesRecycleViewAdapter? = null

    var presenter  = MainScreenFragmentPresenter()

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

        presenter!!.init(getActivity()!!)
        val dividerItemDecoration = DividerItemDecoration(paired_device_list.getContext(),
                LinearLayoutManager.VERTICAL)
        paired_device_list.addItemDecoration(dividerItemDecoration)
        paired_device_list.layoutManager = LinearLayoutManager(getActivity())
        device_list.addItemDecoration(dividerItemDecoration)
        device_list.layoutManager = LinearLayoutManager(getActivity())

        pairedDeviceList()

        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        filter.addAction(BluetoothDevice.ACTION_UUID)
        val mReceiver = SearchDeviceBroadcastReciver(this,presenter!!)
        getActivity()!!.registerReceiver(mReceiver, filter)
        mDevicesRecycleViewAdapter = DevicesRecycleViewAdapter(arrayOfFoundBTDevices, getActivity()!!)
        device_list.adapter = mDevicesRecycleViewAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                if (presenter!!.isAdapterEnabled()) {
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
            0 -> scan_button.text = getString(R.string.no_result)
            1 -> scan_button.text = getString(R.string.result)
            2 -> scan_button.text = getString(R.string.scan)
        }
    }

    override fun setHeaderText(state: Int) {
        when (state) {
            0 -> header.text = getString(R.string.paired_devices_no_result)
            1 -> header.text = getString(R.string.enable)
        }
    }

    override fun setRecyclerAdapter(listOFDevices: ArrayList<BluetoothDevice>) {
        paired_device_list.adapter = DevicesRecycleViewAdapter(listOFDevices, getActivity()!!)
    }

    override fun isDeviceListEmpty ():Boolean{
        return arrayOfFoundBTDevices.isEmpty()
    }

    override fun getFirstDeviceFromList ():BluetoothDevice{
        return arrayOfFoundBTDevices[0];
    }

    override fun updateNearbyList(dev : BluetoothDevice){
        var remove = ArrayList<BluetoothDevice>()
        var index = ArrayList<Int>()
        if (mDevicesRecycleViewAdapter!!.devices.size>0){
            for (i in mDevicesRecycleViewAdapter!!.devices){
                if (i.address == dev.address){
                    remove.add(i) //mDevicesRecycleViewAdapter!!.devices.indexOf(i))
                }
            }
            mDevicesRecycleViewAdapter!!.devices.removeAll(remove)
//            mDevicesRecycleViewAdapter!!.notifyItemRemoved(index)
        }
        mDevicesRecycleViewAdapter!!.add(dev)
    }

    fun pairedDeviceList() {
        var paired_devices = presenter!!.getPairedDeviceList()
        if (!paired_devices.isEmpty()) {
            val listOFDevices: ArrayList<BluetoothDevice> = ArrayList()
            for (device: BluetoothDevice in paired_devices) {
                listOFDevices.add(device)
            }
            setRecyclerAdapter(listOFDevices)
        } else {
            setHeaderText(0)
        }
    }
}