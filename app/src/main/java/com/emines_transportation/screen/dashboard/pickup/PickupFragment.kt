package com.emines_transportation.screen.dashboard.pickup

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
import com.emines_transportation.databinding.FragmentBuyerBinding
import com.emines_transportation.model.response.TransporterOrderResponse
import com.emines_transportation.network.ApiResponse
import com.emines_transportation.screen.dashboard.pickup.detail.PickUpDetailActivity

import com.emines_transportation.util.Constants
import com.emines_transportation.util.mToast
import org.koin.core.component.inject

class PickupFragment : BaseFragment(), OnClickPickupListener {

    private lateinit var mBind: FragmentBuyerBinding
    private val mViewModel: PickupViewModel by inject()
    private var mContext: Context? = null
    private var transporterOrdersList = mutableListOf<TransporterOrderResponse>()

    override fun getLayoutId() = R.layout.fragment_buyer


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }


    override fun onCreateViewInit(binding: ViewDataBinding, view: View) {
        mBind = binding as FragmentBuyerBinding
        showTab()
        mBind.apply {
            toolbarBuyers.apply {
                ivToolBarBack.visibility = View.GONE
                tvToolBarTitle.apply {
                    setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
                    text = getString(R.string.buyer_list)
                }

            }

        }
    }

    private fun hitOrderApi(status: String) {
        mViewModel.hitPickedUpOrderListApi(mPref.getUserDetail().id, status)
        mViewModel.getPickedUpOrderListResponse()
            .observe(requireActivity(), transporterOrderObserver)
    }

    private fun setOrderList(pickupList: MutableList<TransporterOrderResponse>) {
        mBind.apply {
            if (pickupList.isEmpty()) {
                rlNoDataAvailable.visibility = View.VISIBLE
                rvBuyer.visibility = View.GONE
            } else {
                rlNoDataAvailable.visibility = View.GONE
                rvBuyer.visibility = View.VISIBLE
            }
            rvBuyer.apply {
                layoutManager = LinearLayoutManager(
                    mContext!!.applicationContext, LinearLayoutManager.VERTICAL, false
                )
                adapter = PickupListAdapter(
                    pickupList,
                    mContext!!.applicationContext,
                    this@PickupFragment
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

                    mBind.toolbarBuyers.tvToolBarTitle.text = String.format(
                        "%s %s", getString(R.string.buyer_list), "(${transporterOrdersList.size})"
                    )
                }

                ApiResponse.Status.ERROR -> {
                    hideProgress()
                    mToast(it.error?.message.toString())
                }
            }
        }
    }


    override fun onClickTotalOrder(model: TransporterOrderResponse) {
        val b = Bundle()
        b.putSerializable(Constants.DefaultConstant.MODEL_DETAIL, model)
        launchActivity(PickUpDetailActivity::class.java, Constants.DefaultConstant.BUNDLE_KEY, b)
    }


    override fun onResume() {
        super.onResume()
        hitOrderApi(getString(R.string.pick_up))
    }
}