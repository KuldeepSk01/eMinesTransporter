package com.emines_transportation.model.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.sql.Driver

data class TransportDashboardResponse(
    @SerializedName("company_name")
    val companyName: String,
    @SerializedName("deliveredOrders")
    val deliveredOrders: Int,
    @SerializedName("inprocessOrders")
    val inprocessOrders: Int,
    @SerializedName("pickupOrders")
    val pickupOrders: Int,
    @SerializedName("driver")
    val driver: TransporterResponse,

    @SerializedName("company_gst_no")
    val companyGstNo: String,
    @SerializedName("company_gst_file")
    val companyGstFile: String,
    @SerializedName("company_pan_no")
    val companyPanNo: String,
    @SerializedName("company_pan_file")
    val companyPanFile: String

):Serializable