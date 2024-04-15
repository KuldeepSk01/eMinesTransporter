package com.emines_transportation.screen.dashboard.home

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.emines_transportation.R
import com.emines_transportation.base.BaseFragment
import com.emines_transportation.base.BaseResponse1
import com.emines_transportation.databinding.FragmentHomeBinding
import com.emines_transportation.model.response.TransportDashboardResponse
import com.emines_transportation.network.ApiResponse
import com.emines_transportation.screen.dashboard.delivered.DeliveredFragment
import com.emines_transportation.screen.dashboard.inprocess.InProcessFragment
import com.emines_transportation.screen.dashboard.pickup.PickupFragment
import com.emines_transportation.util.Constants
import com.emines_transportation.util.isConnectionAvailable
import com.emines_transportation.util.mToast
import org.koin.core.component.inject

class HomeFragment : BaseFragment() {

    private lateinit var homeBinding: FragmentHomeBinding

    private val homeVM: HomeViewModel by inject()
    private val userDetail = mPref.getUserDetail()

    private lateinit var mContext : Context


    override fun getLayoutId() = R.layout.fragment_home


    override fun onCreateViewInit(binding: ViewDataBinding, view: View) {
        homeBinding = binding as FragmentHomeBinding
        // mViewModel.mFragment = this@HomeFragment
        //homeBinding.mViewModel = mViewModel
        homeBinding.apply {
            Glide.with(requireActivity())
                .load("https://assets-global.website-files.com/60a4a4fb42c31d8c7ae0d7bd/61b0bb04360dff3c34314704_What%20Is%20Freight%20Shipping.jpg")
                .into(ivHome)

            llcHomePickup.setOnClickListener {
              //  replaceFragment(R.id.flMainContainer,PickupFragment())
            }

            llcHomeInProcess.setOnClickListener {
             // replaceFragment(R.id.flMainContainer,InProcessFragment())
            }
            llcHomeDelivered.setOnClickListener {
              //  replaceFragment(R.id.flMainContainer,DeliveredFragment())
            }

            if (isConnectionAvailable()){
                homeVM.hitTransporterDashboard(mPref.getUserDetail().id)
                homeVM.getTransporterDashboard()
                    .observe(requireActivity(), transporterDashboardResponseObserver)

            }else{
                mToast(getString(R.string.no_internet_available))
            }







        userDetail?.let {
                tvProfileHomeUserName.text = it.name
                if (it.profile_pic.isNullOrEmpty()){
                    Glide.with(requireActivity()).load(Constants.DefaultConstant.TRANSPORTER_IMAGE).into(ivProfileHomeImg)
                }else{
                    Glide.with(requireActivity()).load(it.profile_pic).into(ivProfileHomeImg)
                }
            }


        }

    }

    private val transporterDashboardResponseObserver: Observer<ApiResponse<BaseResponse1<TransportDashboardResponse>>> by lazy {
        Observer {
            when (it.status) {
                ApiResponse.Status.LOADING -> {
                    showProgress()
                }

                ApiResponse.Status.SUCCESS -> {
                    hideProgress()
                    homeBinding.apply {
                        val driver = it.data?.data?.driver!!

                        if (driver.profile_pic.isEmpty()){
                            Glide.with(requireActivity()).load(Constants.DefaultConstant.TRANSPORTER_IMAGE).into(ivProfileHomeImg)
                        }else{
                            Glide.with(requireActivity()).load(driver.profile_pic).into(ivProfileHomeImg)
                        }

                        mPref.setUserDetail(driver)

                        it.data?.data?.let {
                            tvProfileHomeCompanyName.text = it.companyName
                            tvPickUpCount.text = it.pickupOrders.toString()
                            tvInProcessCount.text = it.inprocessOrders.toString()
                            tvDeliveredOrderCount.text = it.deliveredOrders.toString()
                            tvCompanyNameHome.text = it.companyName


                            mPref.put(Constants.PreferenceConstant.C_NAME,it.companyName)
                            mPref.put(Constants.PreferenceConstant.C_PAN,it.companyPanNo)
                            mPref.put(Constants.PreferenceConstant.C_PAN_FILE,it.companyPanFile)
                            mPref.put(Constants.PreferenceConstant.C_GST,it.companyGstNo)
                            mPref.put(Constants.PreferenceConstant.C_GST_FILE,it.companyGstFile)

                        }


                    }
                }

                ApiResponse.Status.ERROR -> {
                    hideProgress()
                    mToast(it.error?.message.toString())
                }
            }
        }
    }

}