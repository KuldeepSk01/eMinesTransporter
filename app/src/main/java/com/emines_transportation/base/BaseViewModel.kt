package com.emines_transportation.base

import androidx.lifecycle.ViewModel
import com.emines_transportation.preference.PreferenceHelper
import com.emines_transportation.util.validation.Validator
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class BaseViewModel : ViewModel(), KoinComponent {
    val validator: Validator by inject()
    val mPref: PreferenceHelper by inject()


}