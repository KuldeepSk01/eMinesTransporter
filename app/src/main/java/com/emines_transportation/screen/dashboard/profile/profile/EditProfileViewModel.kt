package com.emines_transportation.screen.dashboard.profile.profile

import androidx.lifecycle.MutableLiveData
import com.emines_transportation.base.BaseResponse1
import com.emines_transportation.base.BaseViewModel
import com.emines_transportation.base.SuccessMsgResponse
import com.emines_transportation.model.response.TransporterResponse
import com.emines_transportation.network.ApiResponse

class EditProfileViewModel(private val repo:EditProfileRepo): BaseViewModel() {
    private val editProfileResponse =
        MutableLiveData<ApiResponse<BaseResponse1<TransporterResponse>>>()

    fun hitEditProfile(transporterId:Int, truckNumber:String) {
        repo.executeEditProfile(truckNumber,transporterId, editProfileResponse)
    }

    fun getEditProfileResponse(): MutableLiveData<ApiResponse<BaseResponse1<TransporterResponse>>> {
        return editProfileResponse
    }

}