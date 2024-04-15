package com.emines_transportation.network

import com.emines_transportation.base.BaseResponse
import com.emines_transportation.base.BaseResponse1
import com.emines_transportation.base.CollectionBaseResponse
import com.emines_transportation.base.SuccessMsgResponse
import com.emines_transportation.model.response.LoginOtpResponse
import com.emines_transportation.model.response.TransportDashboardResponse
import com.emines_transportation.model.response.TransporterOrderDetailResponse
import com.emines_transportation.model.response.TransporterOrderResponse
import com.emines_transportation.model.response.TransporterResponse
import com.emines_transportation.util.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @POST(Constants.UrlsEndPoint.login)
    fun login(@Query("mobile") mobile: String): Call<LoginOtpResponse>

    @POST(Constants.UrlsEndPoint.verifyOtp)
    fun verifyOtp(
        @Query("mobile") mobile: String,
        @Query("otp") otp: String
    ): Call<BaseResponse<TransporterResponse>>


    @FormUrlEncoded
    @POST(Constants.UrlsEndPoint.transporterOrders)
    fun getTransporterOrder(
        @Field("transporter_id") transporterId: Int,
        @Field("status") status: String
    ): Call<CollectionBaseResponse<TransporterOrderResponse>>

    @FormUrlEncoded
    @POST(Constants.UrlsEndPoint.orderInfoInTransporter)
    fun getTransporterOrderDetail(
        @Field("transporter_id") transporterId: Int,
        @Field("order_id") orderId: Int
    ): Call<CollectionBaseResponse<TransporterOrderDetailResponse>>


    @FormUrlEncoded
    @POST(Constants.UrlsEndPoint.changeOrderStatusByTransporter)
    fun changeOrderStatusByTransporter(
        @Field("transporter_id") transporterId: Int,
        @Field("order_id") orderId: Int,
        @Field("status") status: String
    ): Call<SuccessMsgResponse>

    @Multipart
    @POST(Constants.UrlsEndPoint.pickedUpByTransporter)
    fun pickedUpOrderByTransporter(
        @Part("transporter_id") transporterId: RequestBody,
        @Part("order_id") orderId: RequestBody,
        @Part("pickup_weight") pickupWeight: RequestBody,
        @Part("bill_number") billNumber: RequestBody,
        @Part weightReceipt:MultipartBody.Part?,
        @Part eWayBill:MultipartBody.Part?
    ): Call<SuccessMsgResponse>

    @Multipart
    @POST(Constants.UrlsEndPoint.deliverByTransporter)
    fun deliveredOrderByTransporter(
        @Part("transporter_id") transporterId: RequestBody,
        @Part("order_id") orderId: RequestBody,
        @Part("delivery_weight") deliveryWeight: RequestBody,
        @Part("grn_number") grnNumber: RequestBody,
        @Part deliveryWeightReceipt:MultipartBody.Part?,
        @Part grnImage:MultipartBody.Part?
    ): Call<SuccessMsgResponse>



    @FormUrlEncoded
    @POST(Constants.UrlsEndPoint.editProfile)
    fun editProfile(
        @Field("truck_number") truckNumber: String,
        @Field("id") id: Int
    ): Call<BaseResponse1<TransporterResponse>>


    @FormUrlEncoded
    @POST(Constants.UrlsEndPoint.transporter_dashboard)
    fun transporterDashboard(@Field("transporter_id") transporterId:Int): Call<BaseResponse1<TransportDashboardResponse>>




    @Multipart
    @POST(Constants.UrlsEndPoint.uploadDeliveryBill)
    fun uploadDeliveredBillTransporter(
        @Part("transporter_id") transporterId: RequestBody,
        @Part("order_id") orderId: RequestBody,
        @Part uploadDeliveryBillFile:MultipartBody.Part?,
    ): Call<SuccessMsgResponse>



}