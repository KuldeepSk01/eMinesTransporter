package com.emines_transportation.screen.dashboard.delivered

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
import com.emines_transportation.databinding.FragmentOrdersBinding
import com.emines_transportation.model.response.TransporterOrderResponse
import com.emines_transportation.network.ApiResponse
import com.emines_transportation.screen.dashboard.delivered.detail.DeliveredDetailActivity
import com.emines_transportation.screen.dashboard.pickup.PickupViewModel
import com.emines_transportation.util.Constants
import com.emines_transportation.util.isConnectionAvailable
import com.emines_transportation.util.mToast
import org.koin.core.component.inject

class DeliveredFragment : BaseFragment(), OnClickPickupListener {

    private lateinit var mBind: FragmentOrdersBinding
    private val mViewModel: PickupViewModel by inject()
    private var transporterOrdersList = mutableListOf<TransporterOrderResponse>()
    private lateinit var mContext: Context

    override fun getLayoutId() = R.layout.fragment_orders
    override fun onCreateViewInit(binding: ViewDataBinding, view: View) {
        mBind = binding as FragmentOrdersBinding
        mBind.apply {

            toolBarOrder.apply {
                ivToolBarBack.visibility = View.GONE
                tvToolBarTitle.apply {
                    setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
                    text = String.format("%s", getString(R.string.orders_text))
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onClickTotalOrder(model: TransporterOrderResponse) {
        val b = Bundle()
        b.putSerializable(Constants.DefaultConstant.MODEL_DETAIL, model)
        launchActivity(DeliveredDetailActivity::class.java, Constants.DefaultConstant.BUNDLE_KEY, b)
    }

    private fun hitOrderApi(status: String) {
        mViewModel.hitDeliveredOrderListApi(mPref.getUserDetail().id, status)
        mViewModel.getDeliveredOrderListResponse()
            .observe(requireActivity(), transporterOrderObserver)
    }

    private fun setOrderList(pickupList: MutableList<TransporterOrderResponse>) {
        mBind.apply {
            if (pickupList.isEmpty()) {
                rlNoDataAvailable.visibility = View.VISIBLE
                rvOrder.visibility = View.GONE
            } else {
                rlNoDataAvailable.visibility = View.GONE
                rvOrder.visibility = View.VISIBLE
            }

            rvOrder.apply {
                layoutManager = LinearLayoutManager(
                    mContext.applicationContext, LinearLayoutManager.VERTICAL, false
                )
                adapter = PickupListAdapter(
                    pickupList,
                    mContext.applicationContext,
                    this@DeliveredFragment
                )
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

                    mBind.toolBarOrder.tvToolBarTitle.text = String.format(
                        "%s %s", getString(R.string.orders_text), "(${transporterOrdersList.size})"
                    )
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
        if (isConnectionAvailable()){
            hitOrderApi(getString(R.string.delivered))
        }else{
            mToast(getString(R.string.no_internet_available))
        }


    }
}