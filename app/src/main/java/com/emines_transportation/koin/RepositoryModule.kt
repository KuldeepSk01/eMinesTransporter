package com.emines_transportation.koin

import com.emines_transportation.base.BaseRepository
import com.emines_transportation.screen.dashboard.MainRepo
import com.emines_transportation.screen.dashboard.home.HomeRepo
import com.emines_transportation.screen.dashboard.pickup.PickupRepo
import com.emines_transportation.screen.dashboard.profile.profile.EditProfileRepo
import com.emines_transportation.screen.login.LoginRepo
import com.emines_transportation.screen.otpverification.OtpVerifyRepo
import org.koin.dsl.module

val repositoryModule = module {
    single { BaseRepository() }
    single { MainRepo() }
    single { HomeRepo() }
    single { LoginRepo() }
    single { OtpVerifyRepo() }
    single { PickupRepo() }
    single { EditProfileRepo() }

}