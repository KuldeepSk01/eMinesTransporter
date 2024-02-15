package com.emines_transportation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.emines_transportation.R
import com.emines_transportation.databinding.ItemOnboardingLayoutBinding
import com.emines_transportation.model.CommonModel

class OnBoardingAdapter(private val list: MutableList<CommonModel>, private val context: Context) :
    RecyclerView.Adapter<OnBoardingAdapter.OnVM>() {
    inner class OnVM(val b: ItemOnboardingLayoutBinding) : ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnVM {
        val view = DataBindingUtil.inflate<ItemOnboardingLayoutBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_onboarding_layout, parent, false
        )

        return OnVM(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: OnVM, position: Int) {
        val model = list[position]
        Glide.with(context).load(model.imgUrl).into(holder.b.ivOnboardingItem)
        holder.b.tvOnboardingTitle.text = model.pageTitle
        holder.b.tvOnboardingSubTitle.text = context.getString(R.string.loading_sub_title_text)
    }

}