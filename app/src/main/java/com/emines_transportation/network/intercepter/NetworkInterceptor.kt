package com.emines_transportation.network.intercepter

import android.text.TextUtils
import com.emines_transportation.preference.PreferenceHelper
import com.emines_transportation.util.Constants.PreferenceConstant.Companion.AUTHORIZATION
import com.emines_transportation.util.Constants.PreferenceConstant.Companion.TOKEN
import com.emines_transportation.util.mLog
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NetworkInterceptor : Interceptor, KoinComponent {
    private val mPref: PreferenceHelper by inject()
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val token = mPref[TOKEN, ""]
        if (TextUtils.isEmpty(token)) {
            return chain.proceed(originalRequest)
        }
        val newRequest = originalRequest
            .newBuilder()
            .addHeader(
                AUTHORIZATION, "Bearer $token"
            )
            .build()
        mLog("Authorization intercept: token--> $token")
        return chain.proceed(newRequest)
    }
}