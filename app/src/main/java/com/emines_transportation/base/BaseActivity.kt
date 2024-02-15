package com.emines_transportation.base

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.emines_transportation.CustomDialogs
import com.emines_transportation.R
import com.emines_transportation.preference.PreferenceHelper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class BaseActivity : AppCompatActivity(), BaseInterface, LifecycleOwner, KoinComponent {

    val mPref: PreferenceHelper by inject()

    private val progressDialog: Dialog by lazy {
        CustomDialogs.successProgressDialog(this@BaseActivity)
    }

    fun showProgress() {
        if (!progressDialog.isShowing) {
            progressDialog.show()
        }
    }

    fun hideProgress() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        //   requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val layout = layoutId
        if (layout != 0) {
            val binding = DataBindingUtil.setContentView<ViewDataBinding>(this, layout)
            onCreateInit(binding)
        }
    }

    fun launchActivity(classType: Class<out BaseActivity>, key: String, value: String) {
        val intent = Intent(this@BaseActivity, classType)
        intent.putExtra(key, value)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)

    }

    fun launchActivity(classType: Class<out BaseActivity>, bundleKey: String, bundle: Bundle) {
        val intent = Intent(this@BaseActivity, classType)
        intent.putExtra(bundleKey, bundle)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)

    }

    fun launchActivity(classType: Class<out BaseActivity>) {
        val intent = Intent(this@BaseActivity, classType)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)

    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
            onBackPressedDispatcher.onBackPressed()
        }
    }


    fun replaceFragment(
        containerId: Int,
        fragment: Fragment,
        b: Bundle,
        addToBackStack: String? = null
    ) {
        val bt = supportFragmentManager.beginTransaction()
        bt.setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
        fragment.arguments = b
        bt.replace(containerId, fragment)
        bt.addToBackStack(addToBackStack)
        bt.commit()

    }

    fun replaceFragment(
        containerId: Int,
        fragment: Fragment
    ) {
        val bt = supportFragmentManager.beginTransaction()
        bt.setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
        bt.replace(containerId, fragment)
        bt.commit()

    }
}