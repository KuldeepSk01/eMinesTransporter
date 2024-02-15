package com.emines_transportation.screen.dashboard

import androidx.lifecycle.MutableLiveData
import com.emines_transportation.base.BaseRepository
import com.emines_transportation.base.BaseResponse
import com.emines_transportation.model.response.TransporterResponse
import com.emines_transportation.network.ApiResponse

class MainRepo : BaseRepository() {
    fun executeProfileApi(
        userId: Int,
        responseLiveData: MutableLiveData<ApiResponse<BaseResponse<TransporterResponse>>>
    ) {
        /*   val call = apiService.employeeProfile(userId)
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
                           val jObjError = JSONObject(
                               response.errorBody()!!.string()
                           )
                           val jObjErrorMessage = jObjError.getString("message")
                           val jObjErrorCode = jObjError.getString("code")
                           mLog(jObjErrorMessage)
                           mLog(jObjErrorCode)
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
     */
    }

}