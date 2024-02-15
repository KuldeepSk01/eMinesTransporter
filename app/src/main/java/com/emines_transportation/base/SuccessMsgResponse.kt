package com.emines_transportation.base

import com.google.gson.annotations.SerializedName

data class SuccessMsgResponse(
    @SerializedName("status") val status: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("code") val code: Long?,
    @SerializedName("data") val data: List<Any>
)
