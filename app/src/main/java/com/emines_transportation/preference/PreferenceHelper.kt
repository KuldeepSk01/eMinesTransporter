package com.emines_transportation.preference

import android.content.SharedPreferences
import com.emines_transportation.model.request.AddBuyerRequest
import com.emines_transportation.model.response.TransporterResponse
import com.emines_transportation.preference.reflection.ReflectionUtil
import com.emines_transportation.util.Constants.PreferenceConstant.Companion.USER_DETAIL
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/*
this class helps for storing and retrieve the small amount of data in device storage
*/
class PreferenceHelper(val sharedPref: SharedPreferences) : KoinComponent {
    private val reflectionUtil: ReflectionUtil by inject()
    fun put(key: String, value: String) {
        sharedPref.edit().putString(key, value).apply()
    }

    fun put(key: String, value: Boolean) {
        sharedPref.edit().putBoolean(key, value).apply()
    }

    fun put(key: String, value: Int) {
        sharedPref.edit().putInt(key, value).apply()
    }

    fun setUserDetail(TransporterResponse: TransporterResponse?) {
        val userDetail = reflectionUtil.parseClassToJson(TransporterResponse!!)

        sharedPref.edit().putString(USER_DETAIL, userDetail).apply()
    }

    fun getUserDetail(): TransporterResponse {
        return reflectionUtil.parseJsonToClass(get(USER_DETAIL, "")!!, TransporterResponse::class.java)
    }



    operator fun get(key: String, defaultValue: String): String? {
        return sharedPref.getString(key, defaultValue)
    }

    operator fun get(key: String, defaultValue: Int): Int {
        return sharedPref.getInt(key, defaultValue)
    }

    operator fun get(key: String, defaultValue: Boolean): Boolean {
        return sharedPref.getBoolean(key, defaultValue)
    }


    fun clearSharedPref() {
        sharedPref.edit().clear().apply()
    }

}