package com.emines_transportation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.emines_transportation.R
import com.emines_transportation.adapter.listener.OnClickPickupListener
import com.emines_transportation.databinding.ItemPickupLayoutBinding
import com.emines_transportation.model.response.TransporterOrderResponse
import com.emines_transportation.util.Constants

class PickupListAdapter(
    val list: MutableList<TransporterOrderResponse>, val context: Context, private val listener: OnClickPickupListener
) :
    RecyclerView.Adapter<PickupListAdapter.PickupSellerVM>() {
    class PickupSellerVM(val b: ItemPickupLayoutBinding) : ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickupSellerVM {
        return PickupSellerVM(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_pickup_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: PickupSellerVM, position: Int) {
        val model = list[position]
        holder.b.apply {
            Glide.with(context).load(Constants.DefaultConstant.DRIVER_ORDER_IMAGE).into(ivItemAddressPickupImg)

            tvItemPickupId.text = String.format("%s%s","EMS000",model.id.toString())
            tvPickupAddress.text = model.destination //this is pickup date
            tvTotalCategoryPickup.text = model.total_order_category
            tvItemPoNo.text = model.po_no
            tvTotalWeightPickup.text = model.total_purchased_quantity
            tvItemPickupDate.text = model.po_date
            tvPODropAddressItemPickupBReq.text = model.address //this is drop location
            tvItemPickupAssignedDate.text = String.format("%s %s","Assigned Date :",model.assigned_date)

            tvOrderStatus.apply {
                when(model.status) {
                    context.getString(R.string.assigned)->{
                        text = String.format("%s, %s",model.status," ${model.assigned_date}")
                        setTextColor(context.getColor(R.color.order_complete_color))
                    }
                    context.getString(R.string.new_order)->{
                        text = model.status
                        setTextColor(context.getColor(R.color.order_complete_color))
                    }
                    context.getString(R.string.reached)->{
                        text = String.format("%s, %s",model.status," ${model.reached_date}")
                        setTextColor(context.getColor(R.color.order_accepted_color))
                    }
                    context.getString(R.string.accepted)->{
                        text = String.format("%s, %s",model.status," ${model.accepte_date}")
                        setTextColor(context.getColor(R.color.order_accepted_color))
                    }
                    context.getString(R.string.delivered)->{
                        tvEstDate.text = "Delivered Date"
                        text = String.format("%s, %s",model.status," ${model.delivery_date}")
                        setTextColor(context.getColor(R.color.order_complete_color))
                    }
                    context.getString(R.string.picked_up)->{
                        text = String.format("%s, %s",model.status," ${model.pickup_date}")
                        setTextColor(context.getColor(R.color.order_picked_color))
                    }
                    context.getString(R.string.denied)->{
                        text = String.format("%s, %s",model.status," ${model.denied_date}")
                        setTextColor(context.getColor(R.color.order_cancel_color))
                    }
                    context.getString(R.string.in_process)->{
                        setTextColor(context.getColor(R.color.order_picked_color))
                    }
                }
            }

            cvItemPickupBReq.setOnClickListener {
                listener.onClickTotalOrder(model)
            }


        }


    }
}
