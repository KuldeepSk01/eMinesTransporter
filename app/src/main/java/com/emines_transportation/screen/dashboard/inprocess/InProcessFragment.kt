package com.emines_transportation.screen.dashboard.inprocess

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.emines_transportation.R
import com.emines_transportation.adapter.PickupListAdapter
import com.emines_transportation.adapter.listener.OnClickPickupListener
import com.emines_transportation.base.BaseFragment
import com.emines_transportation.base.CollectionBaseResponse
import com.emines_transportation.databinding.FragmentSellerBinding
import com.emines_transportation.model.response.TransporterOrderResponse
import com.emines_transportation.network.ApiResponse
import com.emines_transportation.screen.dashboard.inprocess.detail.InProcessDetailActivity
import com.emines_transportation.screen.dashboard.pickup.PickupViewModel
import com.emines_transportation.util.Constants
import com.emines_transportation.util.mToast
import org.koin.core.component.inject

class InProcessFragment : BaseFragment(), OnClickPickupListener {

    private lateinit var mBind: FragmentSellerBinding
    private val mViewModel: PickupViewModel by inject()
    private var transporterOrdersList = mutableListOf<TransporterOrderResponse>()
    private var mContext: Context?=null

    override fun getLayoutId() = R.layout.fragment_seller

    override fun onCreateViewInit(binding: ViewDataBinding, view: View) {
        mBind = binding as FragmentSellerBinding
        mBind.apply {

            toolbarInProcess.apply {
                ivToolBarBack.visibility = View.GONE
                tvToolBarTitle.apply {
                    setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
                    text = String.format(
                        "%s", getString(R.string.seller_list))
                }
            }

        }
    }

    override fun onClickTotalOrder(model: TransporterOrderResponse) {
        val b = Bundle()
        b.putSerializable(Constants.DefaultConstant.MODEL_DETAIL, model)
        launchActivity(InProcessDetailActivity::class.java, Constants.DefaultConstant.BUNDLE_KEY,b)
      //  launchActivity(InProcessDetailActivity::class.java)
    }

    private fun hitOrderApi(status: String) {
        mViewModel.hitInProcessOrderListApi(mPref.getUserDetail().id,status)
        mViewModel.getInProcessOrderListResponse().observe(requireActivity(),transporterOrderObserver)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private fun setOrderList(pickupList: MutableList<TransporterOrderResponse>) {

        mBind.apply {
            if (pickupList.isEmpty()) {
                rlNoDataAvailable.visibility = View.VISIBLE
                rvInProcesss.visibility = View.GONE
            } else {
                rlNoDataAvailable.visibility = View.GONE
                rvInProcesss.visibility = View.VISIBLE
            }
            rvInProcesss.apply {
                layoutManager =
                    LinearLayoutManager(mContext!!.applicationContext, LinearLayoutManager.VERTICAL, false)
                adapter = PickupListAdapter(pickupList, mContext!!.applicationContext,this@InProcessFragment)
            }
        }


    }

    private val transporterOrderObserver: Observer<ApiResponse<CollectionBaseResponse<TransporterOrderResponse>>> by lazy {
        Observer {
            when (it.status) {
                ApiResponse.Status.LOADING -> {
                    showProgress()
                }

                ApiResponse.Status.SUCCESS -> {
                    hideProgress()
                    transporterOrdersList.clear()
                    transporterOrdersList = it.data?.data as MutableList<TransporterOrderResponse>
                    setOrderList(transporterOrdersList)
                    mBind.toolbarInProcess.tvToolBarTitle.text = String.format("%s %s",getString(R.string.seller_list),"(${transporterOrdersList.size})")
                }

                ApiResponse.Status.ERROR -> {
                    hideProgress()
                    mToast(it.error?.message.toString())
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        hitOrderApi(getString(R.string.in_process))
    }



}