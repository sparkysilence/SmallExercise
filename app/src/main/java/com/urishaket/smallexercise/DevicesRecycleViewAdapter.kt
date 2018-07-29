package com.urishaket.smallexercise


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.bt_list_item.view.*
import android.bluetooth.BluetoothDevice
import android.util.Log

class DevicesRecycleViewAdapter(val devices: ArrayList<BluetoothDevice>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {
    private val TAG = "Recycle Adapter"

    override fun getItemCount(): Int {
        return devices.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.bt_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bt_device?.text = devices.get(position).name
        if (devices.get(position).uuids!=null){
        holder?.bt_device_uuid?.text =devices.get(position).uuids[0].toString()}else{
            holder?.bt_device_uuid?.text =devices.get(position).address
        }
    }

    fun clear() {
        val size  = devices.size
        devices.clear()
        notifyItemRangeRemoved(0,size)
        notifyDataSetChanged()
        Log.i(TAG,"item count: " + getItemCount())
    }

    fun add(dev:BluetoothDevice){
        devices.add(dev)
        notifyDataSetChanged()
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val bt_device = view.bluetooth_device
    val bt_device_uuid = view.bluetooth_device_uuid
}