package com.emines_transportation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.emines_transportation.R
import com.emines_transportation.databinding.ItemDutyCompleteLayoutBinding

class DutyCompleteAdapter(private val list: MutableList<String>, val context: Context) :
    RecyclerView.Adapter<DutyCompleteAdapter.DutyCompleteVM>() {
    inner class DutyCompleteVM(val b: ItemDutyCompleteLayoutBinding) : ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DutyCompleteVM {
        return DutyCompleteVM(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_duty_complete_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: DutyCompleteVM, position: Int) {
        val model = list[position]
        holder.b.tvEnterTruckNumber.text = "UP16CC9276"
    }

}