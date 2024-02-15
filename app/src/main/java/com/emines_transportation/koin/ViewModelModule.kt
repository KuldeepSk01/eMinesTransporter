package com.emines_transportation.koin

import com.emines_transportation.base.BaseViewModel
import com.emines_transportation.screen.dashboard.MainViewModel
import com.emines_transportation.screen.dashboard.home.HomeViewModel
import com.emines_transportation.screen.dashboard.pickup.PickupViewModel
import com.emines_transportation.screen.dashboard.profile.profile.EditProfileViewModel
import com.emines_transportation.screen.login.LoginViewModel
import com.emines_transportation.screen.onboarding.OnBoardViewModel
import com.emines_transportation.screen.otpverification.OTPVerifyViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { BaseViewModel() }
    single { MainViewModel(get()) }
    single { HomeViewModel(get()) }
    single { LoginViewModel(get()) }
    single { OnBoardViewModel() }
    single { OTPVerifyViewModel(get()) }
    single { PickupViewModel(get()) }
    single { EditProfileViewModel(get()) }

}