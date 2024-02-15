package com.emines_transportation.screen.dashboard.profile.profile

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.emines_transportation.R
import com.emines_transportation.base.BaseActivity
import com.emines_transportation.base.BaseResponse1
import com.emines_transportation.databinding.ActivityProfileInfoBinding
import com.emines_transportation.model.response.TransporterResponse
import com.emines_transportation.network.ApiResponse
import com.emines_transportation.util.Constants
import com.emines_transportation.util.mToast
import org.koin.core.component.inject

class ProfileInfoActivity : BaseActivity() {
    private lateinit var profileBinding: ActivityProfileInfoBinding
    private val userDetail = mPref.getUserDetail()
    private val mViewMode: EditProfileViewModel by inject()

    override val layoutId = R.layout.activity_profile_info

    override fun onCreateInit(binding: ViewDataBinding?) {
        profileBinding = binding as ActivityProfileInfoBinding
        profileBinding.apply {
            toolbarBBA.tvToolBarTitle2.text = getString(R.string.profile_info)
            bottomButtons.apply {
                tvFirstBtn.apply {
                    text = getString(R.string.back_cap)
                    setOnClickListener {
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
                tvSecondBtn.apply {
                    text = getString(R.string.save_cap)
                    setOnClickListener {
                        val truckNO = etTruckNumber.text.toString()
                        mViewMode.hitEditProfile(mPref.getUserDetail().id, truckNO)
                        mViewMode.getEditProfileResponse()
                            .observe(this@ProfileInfoActivity, editProfileResponseObserver)
                    }
                }
            }

            userDetail?.let {
                etFNamePI.text = it.name
                etPhoneNumberPI.text = it.phone
                etTruckNumber.setText(it.truck_number)
                if (it.profile_pic.isEmpty()){
                    Glide.with(this@ProfileInfoActivity).load(Constants.DefaultConstant.TRANSPORTER_IMAGE).into(ivPI)
                }else{
                    Glide.with(this@ProfileInfoActivity).load(it.profile_pic).into(ivPI)
                }

            }

        }
    }


    private val editProfileResponseObserver =
        Observer<ApiResponse<BaseResponse1<TransporterResponse>>> {
            when (it.status) {
                ApiResponse.Status.LOADING -> {
                    showProgress()
                }

                ApiResponse.Status.SUCCESS -> {
                    hideProgress()
                    mPref.setUserDetail(it.data?.data)
                    mToast(it.data?.message.toString())
                    onBackPressedDispatcher.onBackPressed()
                }

                ApiResponse.Status.ERROR -> {
                    hideProgress()
                    mToast(it.error?.message.toString())
                }
            }
        }

}