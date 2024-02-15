package com.emines_transportation.screen.home2

import androidx.databinding.ViewDataBinding
import com.emines_transportation.R
import com.emines_transportation.base.BaseActivity
import com.emines_transportation.databinding.Home2Binding

class Home2Activity : BaseActivity() {

    private lateinit var homeBinding: Home2Binding
    override val layoutId: Int
        get() = R.layout.home2

    override fun onCreateInit(binding: ViewDataBinding?) {
        homeBinding = binding as Home2Binding

    }

}