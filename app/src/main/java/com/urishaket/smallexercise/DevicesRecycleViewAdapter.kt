package com.urishaket.smallexercise


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.bt_list_item.view.*

class DevicesRecycleViewAdapter(val ids : ArrayList<String>,val uuids : ArrayList<String>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return ids.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.bt_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.bt_device?.text = ids.get(position)
        holder?.bt_device_uuid?.text = uuids.get(position)
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val bt_device = view.bluetooth_device
    val bt_device_uuid = view.bluetooth_device_uuid
}