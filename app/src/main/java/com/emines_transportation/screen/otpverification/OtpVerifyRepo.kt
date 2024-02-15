package com.emines_transportation.screen.otpverification

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.emines_transportation.base.BaseRepository
import com.emines_transportation.base.BaseResponse
import com.emines_transportation.model.response.TransporterResponse
import com.emines_transportation.network.ApiResponse
import com.emines_transportation.util.Constants
import com.emines_transportation.util.mLog
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpVerifyRepo : BaseRepository() {
    fun executeVerifyOtpApi(
        mobile: String,
        otp: String,
        responseLiveData: MutableLiveData<ApiResponse<BaseResponse<TransporterResponse>>>
    ) {
        val call = apiService.verifyOtp(mobile, otp)
        responseLiveData.value = ApiResponse.loading()
        call.enqueue(object : Callback<BaseResponse<TransporterResponse>> {
            override fun onResponse(
                call: Call<BaseResponse<TransporterResponse>>,
                response: Response<BaseResponse<TransporterResponse>>
            ) {

                try {
                    if (response.code() == Constants.NetworkConstant.API_SUCCESS) {
                        responseLiveData.postValue(ApiResponse.success(response.body()!!))
                    } else {
                        val jObjErrorMessage = JSONObject(
                            response.errorBody()!!.string()
                        ).getString("message")
                        mLog(jObjErrorMessage)
                        responseLiveData.postValue(ApiResponse.error(Throwable(jObjErrorMessage)))
                    }
                } catch (e: Exception) {
                    responseLiveData.postValue(ApiResponse.error(Throwable(e.message)))
                }

            }

            override fun onFailure(call: Call<BaseResponse<TransporterResponse>>, t: Throwable) {
                if (t.message.equals("Software caused connection abort")) {
                    responseLiveData.postValue(ApiResponse.error(Throwable(Constants.NetworkConstant.CONNECTION_LOST)))
                } else {
                    Log.d("LoginRepo", "onFailure: ${t.message}")
                    responseLiveData.postValue(ApiResponse.error(t))
                }
            }

        })
    }

}