package com.emines_transportation.screen.home2.duty

import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.emines_transportation.R
import com.emines_transportation.adapter.DutyCompleteAdapter
import com.emines_transportation.base.BaseActivity
import com.emines_transportation.databinding.ActivityDutyBinding
import com.google.android.material.tabs.TabLayout

class DutyActivity : BaseActivity(), TabLayout.OnTabSelectedListener {
    private lateinit var dutyBinding: ActivityDutyBinding
    override val layoutId: Int
        get() = R.layout.activity_duty

    override fun onCreateInit(binding: ViewDataBinding?) {
        dutyBinding = binding as ActivityDutyBinding
        dutyBinding.apply {
            dutyTabLayout.selectTab(dutyTabLayout.getTabAt(0))

            dutyTabLayout.addOnTabSelectedListener(this@DutyActivity)
            rvCompleteDuty.apply {
                itemAnimator = DefaultItemAnimator()
                layoutManager =
                    LinearLayoutManager(this@DutyActivity, LinearLayoutManager.VERTICAL, false)
                adapter = DutyCompleteAdapter(getSliderList(), this@DutyActivity)
            }


        }


    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        Log.d("DutyFragment", "onTabSelected: item text ${tab?.text}")
        when (tab?.text) {
            getString(R.string.ongoing) -> {
                dutyBinding.rlDutyOnGoingLayout.visibility = View.VISIBLE
                dutyBinding.rvCompleteDuty.visibility = View.GONE
            }

            getString(R.string.complete) -> {
                dutyBinding.rlDutyOnGoingLayout.visibility = View.GONE
                dutyBinding.rvCompleteDuty.visibility = View.VISIBLE
            }
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    private fun getSliderList(): MutableList<String> {
        val list = mutableListOf<String>()
        list.add(getString(R.string.explore_info))
        list.add(getString(R.string.explore_info))
        list.add(getString(R.string.explore_info))
        list.add(getString(R.string.explore_info))
        return list
    }
}