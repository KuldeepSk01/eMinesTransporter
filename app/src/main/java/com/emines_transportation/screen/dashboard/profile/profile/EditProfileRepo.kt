package com.emines_transportation.screen.dashboard.profile.profile

import androidx.lifecycle.MutableLiveData
import com.emines_transportation.base.BaseRepository
import com.emines_transportation.base.BaseResponse1
import com.emines_transportation.base.SuccessMsgResponse
import com.emines_transportation.model.response.TransporterResponse
import com.emines_transportation.network.ApiResponse
import com.emines_transportation.util.Constants
import com.emines_transportation.util.mLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileRepo : BaseRepository() {

    fun executeEditProfile(
        truckNumber: String,
        transporterId: Int,
        responseLiveData: MutableLiveData<ApiResponse<BaseResponse1<TransporterResponse>>>
    ) {
        val call = apiService.editProfile(truckNumber,transporterId)
        responseLiveData.value = ApiResponse.loading()
        call.enqueue(object : Callback<BaseResponse1<TransporterResponse>> {
            override fun onResponse(
                call: Call<BaseResponse1<TransporterResponse>>,
                response: Response<BaseResponse1<TransporterResponse>>
            ) {
                try {
                    if (response.body()?.status!!) {
                        responseLiveData.postValue(ApiResponse.success(response.body()!!))
                    } else {
                        responseLiveData.postValue(ApiResponse.error(Throwable(response.body()!!.message)))
                    }
                } catch (e: Exception) {
                    responseLiveData.postValue(ApiResponse.error(Throwable(e.message)))
                }

            }

            override fun onFailure(
                call: Call<BaseResponse1<TransporterResponse>>,
                t: Throwable
            ) {
                if (t.message.equals("Software caused connection abort")) {
                    responseLiveData.postValue(ApiResponse.error(Throwable(Constants.NetworkConstant.CONNECTION_LOST)))
                } else {
                    mLog("Buyers Repo onFailure: ${t.message}")
                    responseLiveData.postValue(ApiResponse.error(t))
                }
            }

        })
    }

}