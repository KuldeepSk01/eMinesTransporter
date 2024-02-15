package com.emines_transportation.model.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.sql.Driver

data class TransportDashboardResponse(
    @SerializedName("company")
    val company: String,
    @SerializedName("deliveredOrders")
    val deliveredOrders: Int,
    @SerializedName("inprocessOrders")
    val inprocessOrders: Int,
    @SerializedName("pickupOrders")
    val pickupOrders: Int,
    @SerializedName("driver")
    val driver: TransporterResponse
):Serializable