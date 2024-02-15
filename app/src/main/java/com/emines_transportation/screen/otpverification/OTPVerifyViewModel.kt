package com.emines_transportation.screen.otpverification

import androidx.lifecycle.MutableLiveData
import com.emines_transportation.base.BaseResponse
import com.emines_transportation.base.BaseViewModel
import com.emines_transportation.model.response.TransporterResponse
import com.emines_transportation.network.ApiResponse

class OTPVerifyViewModel(private val repo: OtpVerifyRepo) : BaseViewModel() {
    var otpVerificationActivity: OTPVerificationActivity? = null

    val verifyOtpResponse = MutableLiveData<ApiResponse<BaseResponse<TransporterResponse>>>()
    fun hitOtpVerifyApi(mobile: String, otp: String) {
        repo.executeVerifyOtpApi(mobile, otp, verifyOtpResponse)
    }

    fun getOtp(): String {
        otpVerificationActivity?.oBinding?.apply {
            val otp =
                etOne.text.toString() + etTwo.text.toString() + etThree.text.toString() + etFour.text.toString()
            if (otp.isEmpty()) {
                return "OTP can't blank !"
            } else if (otp.length == 4) {
                return otp
            } else {
                return "OTP must be 4 digit !"
            }
        }
        return "OTP must be 4 digit !"
    }


}