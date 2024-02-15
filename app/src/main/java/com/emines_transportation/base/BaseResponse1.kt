package com.emines_transportation.base

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BaseResponse1<T>(
    @SerializedName("status") val status: Boolean? = false,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: T
) : Serializable
