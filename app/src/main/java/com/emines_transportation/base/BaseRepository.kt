package com.emines_transportation.base

import com.emines_transportation.network.ApiService
import com.emines_transportation.preference.reflection.ReflectionUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class BaseRepository : KoinComponent {
    val apiService: ApiService by inject()
    val reflectionUtil: ReflectionUtil by inject()

}