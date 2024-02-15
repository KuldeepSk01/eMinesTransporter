package com.emines_transportation.screen.dashboard.home

import androidx.lifecycle.MutableLiveData
import com.emines_transportation.base.BaseResponse1
import com.emines_transportation.base.BaseViewModel
import com.emines_transportation.model.response.TransportDashboardResponse
import com.emines_transportation.network.ApiResponse

class HomeViewModel(private val repo: HomeRepo) : BaseViewModel() {
    private val transporterDashboard =
        MutableLiveData<ApiResponse<BaseResponse1<TransportDashboardResponse>>>()

    fun hitTransporterDashboard(transporterId: Int) {
        repo.executeHomeApi(transporterId, transporterDashboard)
    }

    fun getTransporterDashboard(): MutableLiveData<ApiResponse<BaseResponse1<TransportDashboardResponse>>> {
        return transporterDashboard
    }


}