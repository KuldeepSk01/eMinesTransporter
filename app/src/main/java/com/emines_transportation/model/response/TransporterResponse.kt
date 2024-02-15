package com.emines_transportation.model.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TransporterResponse(
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("profile_pic")
    val profile_pic: String,
    @SerializedName("transporter_type")
    val transporter_type: String,
    @SerializedName("truck_number")
    val truck_number: String,
    @SerializedName("updated_at")
    val updated_at: String,
    @SerializedName("user_id")
    val user_id: String
) : Serializable