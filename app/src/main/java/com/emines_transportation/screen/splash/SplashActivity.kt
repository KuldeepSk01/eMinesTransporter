package com.emines_transportation.screen.splash

import android.os.Handler
import android.os.Looper
import androidx.databinding.ViewDataBinding
import com.emines_transportation.R
import com.emines_transportation.base.BaseActivity
import com.emines_transportation.databinding.ActivitySplashBinding
import com.emines_transportation.screen.dashboard.MainActivity
import com.emines_transportation.screen.onboarding.OnBoardingActivity
import com.emines_transportation.util.Constants
import com.intuit.sdp.BuildConfig

class SplashActivity : BaseActivity() {
    private lateinit var splashBinding: ActivitySplashBinding
    private lateinit var mRunnable: Runnable
    private lateinit var mHandler: Handler

    override val layoutId = R.layout.activity_splash

    override fun onCreateInit(binding: ViewDataBinding?) {
        splashBinding = binding as ActivitySplashBinding
      //  splashBinding.tvAppVersion.text = String.format("%s %s",getString(R.string.version1_0),BuildConfig.VERSION_NAME.toString())
        splashBinding.tvAppVersion.text = String.format("%s %s",getString(R.string.version1_0),"1.0")

        mHandler = Handler(Looper.getMainLooper())
        mRunnable = Runnable {
            if (mPref[Constants.PreferenceConstant.IS_LOGIN, 0] == 1) {
                launchActivity(MainActivity::class.java)
                finish()
            } else {
                launchActivity(OnBoardingActivity::class.java)
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mHandler.postDelayed(mRunnable, 3000)
    }


    override fun onPause() {
        super.onPause()
        mHandler.removeCallbacks(mRunnable)
    }

    override fun onStop() {
        super.onStop()
        mHandler.removeCallbacks(mRunnable)
    }
}