package com.emines_transportation.screen.dashboard.profile

import android.view.View
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.emines_transportation.R
import com.emines_transportation.base.BaseFragment
import com.emines_transportation.databinding.FragmentProfileBinding
import com.emines_transportation.screen.dashboard.profile.profile.ProfileInfoActivity
import com.emines_transportation.screen.login.LoginActivity
import com.emines_transportation.util.Constants

class ProfileFragment : BaseFragment() {
    private lateinit var profileFragment: FragmentProfileBinding
    private val userDetail = mPref.getUserDetail()


    override fun getLayoutId() = R.layout.fragment_profile
    override fun onCreateViewInit(binding: ViewDataBinding, view: View) {
        profileFragment = binding as FragmentProfileBinding
        profileFragment.apply {
            tvProfile.setOnClickListener {
                launchActivity(ProfileInfoActivity::class.java)
            }
           /* tvAddressProfile.setOnClickListener {
                launchActivity(ProfileAddressListActivity::class.java)
            }
            tvKYCProfile.setOnClickListener {
                launchActivity(ProfileKYCActivity::class.java)
            }
            tvBankDetailProfile.setOnClickListener {
                launchActivity(ProfileAccountActivity::class.java)
            }*/
            /* tvAttendanceProfile.setOnClickListener {
                 launchActivity(AttendanceSelectDateActivity::class.java)
             }*/

          /*  tvSupportProfile.setOnClickListener {
                launchActivity(PostActivity::class.java)
            }*/
            tvLogoutProfile.setOnClickListener {
                mPref.clearSharedPref()
                launchActivity(LoginActivity::class.java)
                requireActivity().finish()

            }
            userDetail?.let {
                tvProfileName.text = it.name
                tvProfileMobile.text = it.phone
                if (it.profile_pic.isEmpty()){
                    Glide.with(requireActivity()).load(Constants.DefaultConstant.TRANSPORTER_IMAGE).into(ivProfileImg)
                }else{
                    Glide.with(requireActivity()).load(it.profile_pic).into(ivProfileImg)
                }
            }


        }

    }

}