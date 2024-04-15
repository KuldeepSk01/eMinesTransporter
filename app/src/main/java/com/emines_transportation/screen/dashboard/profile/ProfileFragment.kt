package com.emines_transportation.screen.dashboard.profile

import android.app.Dialog
import android.view.View
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.emines_transportation.CustomDialogs
import com.emines_transportation.R
import com.emines_transportation.base.BaseFragment
import com.emines_transportation.databinding.FragmentProfileBinding
import com.emines_transportation.screen.dashboard.profile.profile.ProfileInfoActivity
import com.emines_transportation.screen.login.LoginActivity
import com.emines_transportation.util.Constants
import com.emines_transportation.util.checkIsImageExtensions
import com.emines_transportation.util.downloadFileFromUrl
import com.emines_transportation.util.mToast

class ProfileFragment : BaseFragment() {
    private lateinit var profileFragment: FragmentProfileBinding
    private val userDetail = mPref.getUserDetail()


    override fun getLayoutId() = R.layout.fragment_profile
    override fun onCreateViewInit(binding: ViewDataBinding, view: View) {
        profileFragment = binding as FragmentProfileBinding
        profileFragment.apply {

            val cName =  mPref[Constants.PreferenceConstant.C_NAME,""]
            val cPan =  mPref[Constants.PreferenceConstant.C_PAN,""]
            val cPANFile =  mPref[Constants.PreferenceConstant.C_PAN_FILE,""]
            val cGST =  mPref[Constants.PreferenceConstant.C_GST,""]
            val cGSTFile =  mPref[Constants.PreferenceConstant.C_GST_FILE,""]

            tvProfileCompanyName.text = cName
            etPanNoPI.apply {
                text = cPan
                setOnClickListener {
                    if (cPANFile.isNullOrEmpty()){
                        mToast(getString(R.string.no_pan_found))
                        return@setOnClickListener
                    }else{
                        if (checkIsImageExtensions(cPANFile)){
                            CustomDialogs.showImageDialog(
                                requireContext(),
                                cPANFile,
                                object : CustomDialogs.OnShowImageDialogListener {
                                    override fun onClickDownload(dialog: Dialog, url: String) {
                                        downloadFileFromUrl(url)
                                        // dialog.dismiss()
                                    }
                                }).show()
                        }else
                        {
                            CustomDialogs.showWebViewDialog(
                                requireContext(),
                                cPANFile,
                                object : CustomDialogs.OnShowImageDialogListener {
                                    override fun onClickDownload(dialog: Dialog, url: String) {
                                        downloadFileFromUrl(url)
                                        // dialog.dismiss()
                                    }
                                }).show()
                        }

                    }

                }
            }

            etGSTNoPI.apply {
                text = cGST
                setOnClickListener {
                    if (cGSTFile.isNullOrEmpty()){
                        mToast(getString(R.string.no_gst_found))
                        return@setOnClickListener
                    }else{
                        if (checkIsImageExtensions(cGSTFile)){
                            CustomDialogs.showImageDialog(
                                requireContext(),
                                cGSTFile,
                                object : CustomDialogs.OnShowImageDialogListener {
                                    override fun onClickDownload(dialog: Dialog, url: String) {
                                        downloadFileFromUrl(url)
                                        // dialog.dismiss()
                                    }
                                }).show()
                        }else
                        {
                            CustomDialogs.showWebViewDialog(
                                requireContext(),
                                cGSTFile,
                                object : CustomDialogs.OnShowImageDialogListener {
                                    override fun onClickDownload(dialog: Dialog, url: String) {
                                        downloadFileFromUrl(url)
                                        // dialog.dismiss()
                                    }
                                }).show()
                        }

                    }

                }
            }


            tvProfile.setOnClickListener {
                launchActivity(ProfileInfoActivity::class.java)
            }

/*
            userDetail?.let {
                etFNamePI.text = it.name
                etPhoneNumberPI.text = it.phone
                etTruckNumber.setText(it.truck_number)
                etPanNoPI.setText("PAN234XX")
                etGRNNoPI.setText("GRN")



                if (it.profile_pic.isEmpty()){
                    Glide.with(this@ProfileInfoActivity).load(Constants.DefaultConstant.TRANSPORTER_IMAGE).into(ivPI)
                }else{
                    Glide.with(this@ProfileInfoActivity).load(it.profile_pic).into(ivPI)
                }

            }
*/

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
                if (it.profile_pic.isNullOrEmpty()){
                    Glide.with(requireActivity()).load(Constants.DefaultConstant.TRANSPORTER_IMAGE).into(ivProfileImg)
                }else{
                    Glide.with(requireActivity()).load(it.profile_pic).into(ivProfileImg)
                }
            }


        }

    }

}