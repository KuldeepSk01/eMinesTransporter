package com.emines_transportation.screen.dashboard.pickup

import androidx.lifecycle.MutableLiveData
import com.emines_transportation.base.BaseRepository
import com.emines_transportation.base.CollectionBaseResponse
import com.emines_transportation.base.SuccessMsgResponse
import com.emines_transportation.model.request.AddBuyerRequest
import com.emines_transportation.model.response.TransporterOrderResponse
import com.emines_transportation.network.ApiResponse
import com.emines_transportation.util.Constants
import com.emines_transportation.util.MultipartHelper
import com.emines_transportation.util.mLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PickupRepo : BaseRepository() {

    fun executeDeliveredOrderByTransporterApi(
        transporterId: Int,
        orderId: Int,
        deliveryWeight: String,
        grnNumber: String,
        deliveredWeightReceipt: String?,
        grnFile: String?,
        responseLiveData: MutableLiveData<ApiResponse<SuccessMsgResponse>>
    ) {
        val call = apiService.deliveredOrderByTransporter(
            MultipartHelper.getRequestBody(transporterId.toString()),
            MultipartHelper.getRequestBody(orderId.toString()),
            MultipartHelper.getRequestBody(deliveryWeight.toString()),
            MultipartHelper.getRequestBody(grnNumber.toString()),
            MultipartHelper.getMultipartData(deliveredWeightReceipt, "delivered_weight_receipt"),
            MultipartHelper.getMultipartData(grnFile, "grn_file")
        )
        responseLiveData.value = ApiResponse.loading()
        call.enqueue(object : Callback<SuccessMsgResponse> {
            override fun onResponse(
                call: Call<SuccessMsgResponse>,
                response: Response<SuccessMsgResponse>
            ) {
                try {
                    if (response.body()?.status!!) {
                        responseLiveData.postValue(ApiResponse.success(response.body()!!))
                    } else {
                        responseLiveData.postValue(ApiResponse.error(Throwable(response.body()!!.message)))
                    }
                } catch (e: Exception) {
                    responseLiveData.postValue(ApiResponse.error(Throwable(e.message)))
                }

            }

            override fun onFailure(
                call: Call<SuccessMsgResponse>,
                t: Throwable
            ) {
                if (t.message.equals("Software caused connection abort")) {
                    responseLiveData.postValue(ApiResponse.error(Throwable(Constants.NetworkConstant.CONNECTION_LOST)))
                } else {
                    mLog("Buyers Repo onFailure: ${t.message}")
                    responseLiveData.postValue(ApiResponse.error(t))
                }
            }

        })
    }


    fun executePickedUpOrderByTransporterApi(
        transporterId: Int,
        orderId: Int,
        weight: String,
        eWayNumber: String,
        uploadWeight: String?,
        uploadEWayBill: String?,
        responseLiveData: MutableLiveData<ApiResponse<SuccessMsgResponse>>
    ) {
        val call = apiService.pickedUpOrderByTransporter(
            MultipartHelper.getRequestBody(transporterId.toString()),
            MultipartHelper.getRequestBody(orderId.toString()),
            MultipartHelper.getRequestBody(weight.toString()),
            MultipartHelper.getRequestBody(eWayNumber.toString()),
            MultipartHelper.getMultipartData(uploadWeight, "weight_receipt"),
            MultipartHelper.getMultipartData(uploadEWayBill, "eway_bill")
        )
        responseLiveData.value = ApiResponse.loading()
        call.enqueue(object : Callback<SuccessMsgResponse> {
            override fun onResponse(
                call: Call<SuccessMsgResponse>,
                response: Response<SuccessMsgResponse>
            ) {
                try {
                    if (response.body()?.status!!) {
                        responseLiveData.postValue(ApiResponse.success(response.body()!!))
                    } else {
                        responseLiveData.postValue(ApiResponse.error(Throwable(response.body()!!.message)))
                    }
                } catch (e: Exception) {
                    responseLiveData.postValue(ApiResponse.error(Throwable(e.message)))
                }

            }

            override fun onFailure(
                call: Call<SuccessMsgResponse>,
                t: Throwable
            ) {
                if (t.message.equals("Software caused connection abort")) {
                    responseLiveData.postValue(ApiResponse.error(Throwable(Constants.NetworkConstant.CONNECTION_LOST)))
                } else {
                    mLog("Buyers Repo onFailure: ${t.message}")
                    responseLiveData.postValue(ApiResponse.error(t))
                }
            }

        })
    }


    fun executeChangeOrderStatusByTransporterApi(
        transporterId: Int,
        orderId: Int,
        status: String,
        responseLiveData: MutableLiveData<ApiResponse<SuccessMsgResponse>>
    ) {
        val call = apiService.changeOrderStatusByTransporter(transporterId, orderId, status)
        responseLiveData.value = ApiResponse.loading()
        call.enqueue(object : Callback<SuccessMsgResponse> {
            override fun onResponse(
                call: Call<SuccessMsgResponse>,
                response: Response<SuccessMsgResponse>
            ) {
                try {
                    if (response.body()?.status!!) {
                        responseLiveData.postValue(ApiResponse.success(response.body()!!))
                    } else {
                        responseLiveData.postValue(ApiResponse.error(Throwable(response.body()!!.message)))
                    }
                } catch (e: Exception) {
                    responseLiveData.postValue(ApiResponse.error(Throwable(e.message)))
                }

            }

            override fun onFailure(
                call: Call<SuccessMsgResponse>,
                t: Throwable
            ) {
                if (t.message.equals("Software caused connection abort")) {
                    responseLiveData.postValue(ApiResponse.error(Throwable(Constants.NetworkConstant.CONNECTION_LOST)))
                } else {
                    mLog("Buyers Repo onFailure: ${t.message}")
                    responseLiveData.postValue(ApiResponse.error(t))
                }
            }

        })
    }


    fun executeTransporterOrderApi(
        transporterId: Int,
        status: String,
        responseLiveData: MutableLiveData<ApiResponse<CollectionBaseResponse<TransporterOrderResponse>>>
    ) {
        val call = apiService.getTransporterOrder(transporterId, status)
        responseLiveData.value = ApiResponse.loading()
        call.enqueue(object : Callback<CollectionBaseResponse<TransporterOrderResponse>> {
            override fun onResponse(
                call: Call<CollectionBaseResponse<TransporterOrderResponse>>,
                response: Response<CollectionBaseResponse<TransporterOrderResponse>>
            ) {
                try {
                    if (response.body()?.status!!) {
                        responseLiveData.postValue(ApiResponse.success(response.body()!!))
                    } else {
                        responseLiveData.postValue(ApiResponse.error(Throwable(response.body()!!.message)))
                    }
                } catch (e: Exception) {
                    responseLiveData.postValue(ApiResponse.error(Throwable(e.message)))
                }

            }

            override fun onFailure(
                call: Call<CollectionBaseResponse<TransporterOrderResponse>>,
                t: Throwable
            ) {
                if (t.message.equals("Software caused connection abort")) {
                    responseLiveData.postValue(ApiResponse.error(Throwable(Constants.NetworkConstant.CONNECTION_LOST)))
                } else {
                    mLog("Buyers Repo onFailure: ${t.message}")
                    responseLiveData.postValue(ApiResponse.error(t))
                }
            }

        })
    }


    fun executeSaveBuyerApi(
        req: AddBuyerRequest,
        responseLiveData: MutableLiveData<ApiResponse<SuccessMsgResponse>>
    ) {
        /*  var panMultipart: MultipartBody.Part? = null
          var aadhaarMultipart: MultipartBody.Part? = null
          var gstMultipart: MultipartBody.Part? = null


          mLog("save buyer request $req")
          val userID = req.userId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
          val buyerType = req.buyer_type.toString().toRequestBody("text/plain".toMediaTypeOrNull())
          val fullName = req.full_name.toString().toRequestBody("text/plain".toMediaTypeOrNull())
          val email = req.email.toString().toRequestBody("text/plain".toMediaTypeOrNull())
          val phone = req.phone.toString().toRequestBody("text/plain".toMediaTypeOrNull())
          val cName = req.company_name.toString().toRequestBody("text/plain".toMediaTypeOrNull())
          val cEmail = req.company_email.toString().toRequestBody("text/plain".toMediaTypeOrNull())
          val cPhone = req.company_phone.toString().toRequestBody("text/plain".toMediaTypeOrNull())
          val cWebsite = req.website.toString().toRequestBody("text/plain".toMediaTypeOrNull())
          val beneficiary = req.beneficiary.toString().toRequestBody("text/plain".toMediaTypeOrNull())
          val accountNo = req.account_no.toString().toRequestBody("text/plain".toMediaTypeOrNull())
          val bankName = req.bank_name.toString().toRequestBody("text/plain".toMediaTypeOrNull())
          val ifscCode = req.ifsc_code.toString().toRequestBody("text/plain".toMediaTypeOrNull())
          val panNo = req.pan_no.toString().toRequestBody("text/plain".toMediaTypeOrNull())
          val aadhaarNo = req.aadhar_no.toString().toRequestBody("text/plain".toMediaTypeOrNull())
          val gstNo = req.gst_no.toString().toRequestBody("text/plain".toMediaTypeOrNull())

          if (req.pan_file_path.isNullOrEmpty()) {
              panMultipart = null
          } else {
              val panFile = File(req.pan_file_path!!)
              val panRequestFile: RequestBody =
                  panFile.asRequestBody("multipart/form-data".toMediaType())
              panMultipart = MultipartBody.Part.createFormData(
                  "pan_file",
                  panFile.name,
                  panRequestFile
              )
          }
          if (req.aadhar_file_path.isNullOrEmpty()) {
              aadhaarMultipart = null
          } else {
              val aadhaarFile = File(req.pan_file_path!!)
              val aadhaarRequestFile: RequestBody =
                  aadhaarFile.asRequestBody("multipart/form-data".toMediaType())
              aadhaarMultipart = MultipartBody.Part.createFormData(
                  "aadhar_file",
                  aadhaarFile.name,
                  aadhaarRequestFile
              )
          }
          if (req.gst_file_path.isNullOrEmpty()) {
              gstMultipart = null
          } else {
              val gstFile = File(req.pan_file_path!!)
              val gstRequestFile: RequestBody =
                  gstFile.asRequestBody("multipart/form-data".toMediaType())
              gstMultipart = MultipartBody.Part.createFormData(
                  "gst_file",
                  gstFile.name,
                  gstRequestFile
              )

          }

          val call = apiService.saveBuyersList(
              userID,
              buyerType,
              fullName,
              email,
              phone,
              cName,
              cEmail,
              cWebsite,
              cPhone,
              beneficiary,
              accountNo,
              bankName,
              ifscCode,
              panNo,
              gstNo,
              aadhaarNo,
              panMultipart,
              aadhaarMultipart,
              gstMultipart
          )
          responseLiveData.value = ApiResponse.loading()
          call.enqueue(object : Callback<SuccessMsgResponse> {
              override fun onResponse(
                  call: Call<SuccessMsgResponse>,
                  response: Response<SuccessMsgResponse>
              ) {
                  try {
                      if (response.body()?.status!!) {
                          responseLiveData.postValue(ApiResponse.success(response.body()!!))
                      } else {
                          responseLiveData.postValue(ApiResponse.error(Throwable(response.body()!!.message)))
                      }
                  } catch (e: Exception) {
                      responseLiveData.postValue(ApiResponse.error(Throwable(e.message)))
                  }

              }

              override fun onFailure(
                  call: Call<SuccessMsgResponse>,
                  t: Throwable
              ) {
                  if (t.message.equals("Software caused connection abort")) {
                      responseLiveData.postValue(ApiResponse.error(Throwable(Constants.NetworkConstant.CONNECTION_LOST)))
                  } else {
                      mLog("Buyers Repo onFailure: ${t.message}")
                      responseLiveData.postValue(ApiResponse.error(t))
                  }
              }

          })

          */
    }


    fun uploadDeliveredBill(
        transporterId: Int,
        orderId: Int,
        deliveredBillReceipt: String?,
        responseLiveData: MutableLiveData<ApiResponse<SuccessMsgResponse>>
    ) {
        val call = apiService.uploadDeliveredBillTransporter(
            MultipartHelper.getRequestBody(transporterId.toString()),
            MultipartHelper.getRequestBody(orderId.toString()),
            MultipartHelper.getMultipartData(deliveredBillReceipt, "upload_delivery_bill_file")
        )
        responseLiveData.value = ApiResponse.loading()
        call.enqueue(object : Callback<SuccessMsgResponse> {
            override fun onResponse(
                call: Call<SuccessMsgResponse>,
                response: Response<SuccessMsgResponse>
            ) {
                try {
                    if (response.body()?.status!!) {
                        responseLiveData.postValue(ApiResponse.success(response.body()!!))
                    } else {
                        responseLiveData.postValue(ApiResponse.error(Throwable(response.body()!!.message)))
                    }
                } catch (e: Exception) {
                    responseLiveData.postValue(ApiResponse.error(Throwable(e.message)))
                }

            }

            override fun onFailure(
                call: Call<SuccessMsgResponse>,
                t: Throwable
            ) {
                if (t.message.equals("Software caused connection abort")) {
                    responseLiveData.postValue(ApiResponse.error(Throwable(Constants.NetworkConstant.CONNECTION_LOST)))
                } else {
                    mLog("Buyers Repo onFailure: ${t.message}")
                    responseLiveData.postValue(ApiResponse.error(t))
                }
            }

        })
    }



}