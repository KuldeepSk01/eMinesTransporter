package com.emines_transportation.screen.dashboard

import androidx.lifecycle.MutableLiveData
import com.emines_transportation.base.BaseResponse
import com.emines_transportation.base.BaseViewModel
import com.emines_transportation.model.response.TransporterResponse
import com.emines_transportation.network.ApiResponse

class MainViewModel(private val repo: MainRepo) : BaseViewModel() {
    val profileResponse = MutableLiveData<ApiResponse<BaseResponse<TransporterResponse>>>()
    fun hitProfileApi(userId: Int) {
        repo.executeProfileApi(userId, profileResponse)
    }

}