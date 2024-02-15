package com.emines_transportation.screen.login

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.emines_transportation.R
import com.emines_transportation.base.BaseActivity
import com.emines_transportation.databinding.ActivityLoginBinding
import com.emines_transportation.model.response.LoginOtpResponse
import com.emines_transportation.network.ApiResponse
import com.emines_transportation.screen.otpverification.OTPVerificationActivity
import com.emines_transportation.util.Constants
import com.emines_transportation.util.isConnectionAvailable
import com.emines_transportation.util.mToast
import com.emines_transportation.util.validation.ValidationResult
import com.emines_transportation.util.validation.ValidationState
import org.koin.core.component.inject

class LoginActivity : BaseActivity() {
    private lateinit var loginBinding: ActivityLoginBinding
    override val layoutId = R.layout.activity_login
    private val mViewModel: LoginViewModel by inject()

    override fun onCreateInit(binding: ViewDataBinding?) {
        loginBinding = binding as ActivityLoginBinding
        loginBinding.apply {

            Glide.with(this@LoginActivity).load("https://i.pinimg.com/originals/e5/6b/84/e56b841924ac729935e858cb59535fb7.png").into(ivLogin)
            tvSendOtpBtn.setOnClickListener {
                 mViewModel.isValidData(etPhoneLogin.text.toString())
                 mViewModel.validationResponseObserver.observe(
                     this@LoginActivity,
                     validationObserver
                 )

            }
        }

    }


    private val validationObserver: Observer<ValidationState> by lazy {
        Observer {
            when (it.msg) {
                ValidationResult.EMPTY_MOBILE_NUMBER, ValidationResult.VALID_MOBILE_NUMBER -> {
                    loginBinding.etPhoneLogin.error = getString(it.code)
                    loginBinding.etPhoneLogin.requestFocus()
                }

                ValidationResult.SUCCESS -> {
                    if (!isConnectionAvailable()) {
                        mToast("Internet not available")
                        return@Observer
                    } else {
                        mViewModel.hitLoginApi(loginBinding.etPhoneLogin.text.toString())
                        mViewModel.loginOTPResponse.observe(this@LoginActivity, loginDataObserver)
                    }
                }

                else -> {}
            }
        }
    }

    private val loginDataObserver: Observer<ApiResponse<LoginOtpResponse>> by lazy {
        Observer {
            when (it.status) {
                ApiResponse.Status.LOADING -> {
                    showProgress()
                }

                ApiResponse.Status.SUCCESS -> {
                    hideProgress()

                    val b = Bundle()
                    b.putSerializable(Constants.DefaultConstant.OTP_DETAIL, it.data!!)
                    launchActivity(
                        OTPVerificationActivity::class.java,
                        Constants.DefaultConstant.BUNDLE_KEY,
                        b
                    )
                    finish()
                }

                ApiResponse.Status.ERROR -> {
                    hideProgress()
                    mToast(it.error?.message!!)
                }
            }
        }
    }

}