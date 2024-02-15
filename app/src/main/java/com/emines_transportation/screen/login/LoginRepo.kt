package com.emines_transportation.screen.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.emines_transportation.base.BaseRepository
import com.emines_transportation.base.SuccessMsgResponse
import com.emines_transportation.model.response.LoginOtpResponse
import com.emines_transportation.network.ApiResponse
import com.emines_transportation.util.Constants
import com.emines_transportation.util.mLog
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginRepo : BaseRepository() {

    fun executeLoginApi(
        mobile: String,
        responseLiveData: MutableLiveData<ApiResponse<LoginOtpResponse>>
    ) {
        val call = apiService.login(mobile)
        responseLiveData.value = ApiResponse.loading()
        call.enqueue(object : Callback<LoginOtpResponse> {
            override fun onResponse(
                call: Call<LoginOtpResponse>,
                response: Response<LoginOtpResponse>
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

            override fun onFailure(call: Call<LoginOtpResponse>, t: Throwable) {
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