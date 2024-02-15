package com.emines_transportation.base

import androidx.databinding.ViewDataBinding

interface BaseInterface {
    val layoutId: Int
    fun onCreateInit(binding: ViewDataBinding?)
}