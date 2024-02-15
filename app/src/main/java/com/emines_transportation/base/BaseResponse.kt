package com.emines_transportation.base

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BaseResponse<T>(
    @SerializedName("code") val status: Int? = 0,
    @SerializedName("status") val success: Boolean? = false,
    @SerializedName("message") val message: String? = null,
    @SerializedName("users") val users: T,
    @SerializedName("data") val data: T
) : Serializable
