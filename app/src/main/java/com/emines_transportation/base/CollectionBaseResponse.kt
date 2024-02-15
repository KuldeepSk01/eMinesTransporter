package com.emines_transportation.base

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CollectionBaseResponse<T>(
    @SerializedName("code") val code: Int? = 0,
    @SerializedName("status") val status: Boolean = false,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: List<T>
) : Serializable
