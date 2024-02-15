package com.emines_transportation.screen.otpverification

import android.app.Dialog
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.emines_transportation.CustomDialogs
import com.emines_transportation.R
import com.emines_transportation.base.BaseActivity
import com.emines_transportation.base.BaseResponse
import com.emines_transportation.databinding.ActivityOtpverificationBinding
import com.emines_transportation.model.response.LoginOtpResponse
import com.emines_transportation.model.response.TransporterResponse
import com.emines_transportation.network.ApiResponse
import com.emines_transportation.screen.dashboard.MainActivity
import com.emines_transportation.screen.login.LoginViewModel
import com.emines_transportation.util.Constants
import com.emines_transportation.util.mToast
import com.emines_transportation.util.serializable
import org.koin.core.component.inject

class OTPVerificationActivity : BaseActivity() {
    lateinit var oBinding: ActivityOtpverificationBinding
    private lateinit var countDownTimer: CountDownTimer

    private val mViewModel: OTPVerifyViewModel by inject()
    private val loginViewModel: LoginViewModel by inject()

    override val layoutId = R.layout.activity_otpverification

    override fun onCreateInit(binding: ViewDataBinding?) {
        oBinding = binding as ActivityOtpverificationBinding
         mViewModel.otpVerificationActivity = this@OTPVerificationActivity
         val otpDetail = intent.getBundleExtra(Constants.DefaultConstant.BUNDLE_KEY)?.serializable<LoginOtpResponse>(Constants.DefaultConstant.OTP_DETAIL)
        oBinding.apply {
             tvOtpNoText.text = String.format("%s %s",getString(R.string.your_otp_is),otpDetail?.otp.toString())
            ivBackBtn.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
             setOtpTimer()
            otpHandler()

            tvResendOtpBtn.setOnClickListener {
                mToast("Resend OTP successfully")
                loginViewModel.hitLoginApi(otpDetail?.mobile.toString())
                loginViewModel.loginOTPResponse.observe(this@OTPVerificationActivity,resendOtpDataObserver)
            }

            tvVerifyOTPBtn.setOnClickListener {
                  countDownTimer.cancel()
                  countDownTimer.onFinish()
                  val otp = mViewModel.getOtp()
                  if (otp.length==4){
                      mViewModel.hitOtpVerifyApi(otpDetail?.mobile.toString(),otp!!)
                      mViewModel.verifyOtpResponse.observe(this@OTPVerificationActivity,verifyDataObserver)
                  }else{
                      mToast(otp)
                  }
            }

        }
    }

    private val
            verifyDataObserver: Observer<ApiResponse<BaseResponse<TransporterResponse>>> by lazy {
        Observer {
            when (it.status) {
                ApiResponse.Status.LOADING -> {
                    showProgress()

                }

                ApiResponse.Status.SUCCESS -> {
                    hideProgress()
                    mPref.setUserDetail(it.data?.users!!)
                   // mPref.put(Constants.PreferenceConstant.TOKEN,it.data.t)
                    mPref.put(Constants.PreferenceConstant.IS_LOGIN, 1)

                    CustomDialogs.showLoginSuccessDialog(this@OTPVerificationActivity, object :
                        CustomDialogs.CustomDialogsListener {
                        override fun onComplete(d: Dialog) {
                            countDownTimer.onFinish()
                            d.dismiss()
                            launchActivity(MainActivity::class.java)
                            finish()
                        }
                    }).show()
                }

                ApiResponse.Status.ERROR -> {
                    hideProgress()
                    mToast(it.error?.message!!)
                }
            }

        }
    }

    private val resendOtpDataObserver: Observer<ApiResponse<LoginOtpResponse>> by lazy {
        Observer {
            when (it.status) {
                ApiResponse.Status.LOADING -> {
                    showProgress()

                }

                ApiResponse.Status.SUCCESS -> {
                    hideProgress()
                    setOtpTimer()
                    oBinding.tvOtpNoText.text = String.format(
                        "%s %s",
                        getString(R.string.your_otp_is),
                        it.data?.otp?.toString()
                    )
                }

                ApiResponse.Status.ERROR -> {
                    hideProgress()
                    mToast(it.error?.message!!)
                }
            }

        }
    }

    private fun setOtpTimer() {
        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(p0: Long) {
                oBinding.tvResendOtpBtn.apply {
                    isClickable = false
                    setTextColor(ResourcesCompat.getColor(resources, R.color.hint_text_color, null))
                }
                oBinding.tvOTPTime.setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.black,
                        null
                    )
                )

                if (p0 / 1000 > 11) {
                    oBinding.tvOTPTime.text = "00:${p0 / 1000}"
                } else {
                    oBinding.tvOTPTime.text = "00:0${p0 / 1000}"
                }

            }

            override fun onFinish() {
                oBinding.tvOTPTime.text = "00:00"
                oBinding.tvResendOtpBtn.apply {
                    isClickable = true
                    setTextColor(ResourcesCompat.getColor(resources, R.color.black, null))
                }
                oBinding.tvOTPTime.setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.hint_text_color,
                        null
                    )
                )


            }

        }
        countDownTimer.start()
    }

    private fun otpHandler() {
        oBinding.apply {
            etOne.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0?.length == 1) {
                        etTwo.requestFocus()
                    }
                }

            })
            etTwo.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0?.length == 1) {
                        etThree.requestFocus()
                    }
                }

            })
            etThree.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0?.length == 1) {
                        etFour.requestFocus()
                    }
                }

            })
            etFour.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0?.length == 1) {
                        etFour.requestFocus()
                    }
                }

            })

            etTwo.setOnKeyListener(object : View.OnKeyListener {
                override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
                    if (p1 == KeyEvent.KEYCODE_DEL && p2?.action == KeyEvent.ACTION_DOWN) {
                        return if (etTwo.text.toString().trim().isEmpty()) {
                            etOne.setSelection(etOne.text?.length!!)
                            etOne.requestFocus()
                            true
                        } else false
                    }
                    return false
                }

            })
            etThree.setOnKeyListener(object : View.OnKeyListener {
                override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
                    if (p1 == KeyEvent.KEYCODE_DEL && p2?.action == KeyEvent.ACTION_DOWN) {
                        return if (etThree.text.toString().trim().isEmpty()) {
                            etTwo.setSelection(etOne.text?.length!!)
                            etTwo.requestFocus()
                            true
                        } else false
                    }
                    return false
                }
            })

            etFour.setOnKeyListener(object : View.OnKeyListener {
                override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
                    if (p1 == KeyEvent.KEYCODE_DEL && p2?.action == KeyEvent.ACTION_DOWN) {
                        return if (etFour.text.toString().trim().isEmpty()) {
                            etThree.setSelection(etOne.text?.length!!)
                            etThree.requestFocus()
                            true
                        } else false
                    }
                    return false
                }
            })
        }


    }


}