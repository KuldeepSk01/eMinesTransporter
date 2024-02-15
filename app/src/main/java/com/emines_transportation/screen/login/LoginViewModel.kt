package com.emines_transportation.screen.login

import androidx.lifecycle.MutableLiveData
import com.emines_transportation.R
import com.emines_transportation.base.BaseViewModel
import com.emines_transportation.model.response.LoginOtpResponse
import com.emines_transportation.network.ApiResponse
import com.emines_transportation.util.validation.ValidationResult
import com.emines_transportation.util.validation.ValidationState

class LoginViewModel(private val repo: LoginRepo) : BaseViewModel() {
    val loginOTPResponse = MutableLiveData<ApiResponse<LoginOtpResponse>>()

    fun hitLoginApi(mobile: String) {
        repo.executeLoginApi(mobile, loginOTPResponse)
    }

    var validationResponseObserver = MutableLiveData<ValidationState>()
    fun isValidData(mobile: String?) {
        if (mobile?.trim()?.let { validator.validMobileNo(mobile) } != ValidationResult.SUCCESS) {
            if (mobile?.trim()
                    ?.let { validator.validMobileNo(mobile) } == ValidationResult.EMPTY_MOBILE_NUMBER
            ) {
                validationResponseObserver.postValue(
                    ValidationState(
                        ValidationResult.EMPTY_MOBILE_NUMBER,
                        R.string.error_mobile_empty
                    )
                )
                return
            }
            if (mobile?.trim()
                    ?.let { validator.validMobileNo(mobile) } == ValidationResult.VALID_MOBILE_NUMBER
            ) {
                validationResponseObserver.postValue(
                    ValidationState(
                        ValidationResult.VALID_MOBILE_NUMBER,
                        R.string.error_mobile_length
                    )
                )
                return
            }
        }
        validationResponseObserver.postValue(
            ValidationState(
                ValidationResult.SUCCESS,
                R.string.verify_success
            )
        )
    }

}