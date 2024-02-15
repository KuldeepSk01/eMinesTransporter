package com.emines_transportation.screen.dashboard.home

import androidx.lifecycle.MutableLiveData
import com.emines_transportation.base.BaseRepository
import com.emines_transportation.base.BaseResponse1
import com.emines_transportation.model.response.TransportDashboardResponse
import com.emines_transportation.network.ApiResponse
import com.emines_transportation.util.Constants
import com.emines_transportation.util.mLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRepo : BaseRepository() {
    fun executeHomeApi(
        transporterId:Int,
        responseLiveData: MutableLiveData<ApiResponse<BaseResponse1<TransportDashboardResponse>>>
    ) {
        val call = apiService.transporterDashboard(transporterId)
        responseLiveData.value = ApiResponse.loading()
        call.enqueue(object : Callback<BaseResponse1<TransportDashboardResponse>> {
            override fun onResponse(
                call: Call<BaseResponse1<TransportDashboardResponse>>,
                response: Response<BaseResponse1<TransportDashboardResponse>>
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
                call: Call<BaseResponse1<TransportDashboardResponse>>,
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