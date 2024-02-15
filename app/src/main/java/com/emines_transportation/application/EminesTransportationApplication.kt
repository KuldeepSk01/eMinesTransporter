package com.emines_transportation.application

import android.app.Application
import com.emines_transportation.koin.appModule
import com.emines_transportation.koin.repositoryModule
import com.emines_transportation.koin.viewModelModule
import com.emines_transportation.network.NetworkConnectionManager
import com.emines_transportation.util.setContext
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class EminesTransportationApplication : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@EminesTransportationApplication)
            androidLogger()
            modules(listOf(appModule, repositoryModule, viewModelModule))
        }
        /*

                if (checkGooglePlayService()) {
                    Log.d("TAG", "onCreate: google play service is Available...")
                } else {
                    Log.d("TAG", "onCreate: google play service is UnAvailable...")
                }
        */


        NetworkConnectionManager(applicationContext)
        setContext(this@EminesTransportationApplication)


    }

    /*private fun checkGooglePlayService(): Boolean {
        val googleAvailability = GoogleApiAvailability.getInstance()
        val result =
            googleAvailability.isGooglePlayServicesAvailable(this@EminesTransportationApplication)
        if (result == ConnectionResult.SUCCESS) {
            return true
        } else if (googleAvailability.isUserResolvableError(result)
        ) {
            *//* val dialog :Dialog = googleAvailability.getErrorDialog(this,result,201,object:DialogInterface.OnCancelListener{
                 override fun onCancel(p0: DialogInterface?) {
                     Toast.makeText(this@StayHookApplication, "User cancel the dialog", Toast.LENGTH_SHORT).show()
                     p0?.dismiss()
                 }
             })
             dialog.show()*//*
        }
        return false
    }*/


}