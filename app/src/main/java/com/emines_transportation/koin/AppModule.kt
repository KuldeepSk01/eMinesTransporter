package com.emines_transportation.koin

import android.content.Context
import com.emines_transportation.application.EminesTransportationApplication
import com.emines_transportation.network.ApiService
import com.emines_transportation.network.intercepter.NetworkInterceptor
import com.emines_transportation.preference.PreferenceHelper
import com.emines_transportation.preference.reflection.ReflectionUtil
import com.emines_transportation.util.Constants
import com.emines_transportation.util.Constants.NetworkConstant.Companion.API_TIMEOUT
import com.emines_transportation.util.Constants.PreferenceConstant.Companion.PREFERENCE_NAME
import com.emines_transportation.util.validation.ValidationHelper
import com.emines_transportation.util.validation.Validator
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    single<Gson> {
        GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setLenient().create()
    }

    single {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(NetworkInterceptor()).addInterceptor(loggingInterceptor)
            //httpClient.addInterceptor(loggingInterceptor)
            .connectTimeout(API_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(API_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(API_TIMEOUT, TimeUnit.MILLISECONDS)

        val okHttpClient = httpClient.build()
        Retrofit.Builder().baseUrl(Constants.NetworkConstant.BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(get() as Gson)).build()


    }

    single { (get<Retrofit>()).create(ApiService::class.java) }

    single {
        PreferenceHelper(
            (androidApplication() as EminesTransportationApplication).getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE
            )
        )
    }

    single {
        ReflectionUtil(get())
    }



    single { ValidationHelper() }
    single { Validator(get()) }

}