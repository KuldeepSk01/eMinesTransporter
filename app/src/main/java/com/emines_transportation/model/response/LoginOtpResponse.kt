package com.emines_transportation.model.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginOtpResponse(
    @SerializedName("message") val message: String? = null,
    @SerializedName("status") val status: Boolean?,
    @SerializedName("otp") val otp: Long? = null,
    @SerializedName("mobile") val mobile: String? = null
) : Serializable
