package com.emines_transportation.screen.dashboard.pickup

import androidx.lifecycle.MutableLiveData
import com.emines_transportation.R
import com.emines_transportation.base.BaseViewModel
import com.emines_transportation.base.CollectionBaseResponse
import com.emines_transportation.base.SuccessMsgResponse
import com.emines_transportation.model.response.TransporterOrderResponse
import com.emines_transportation.network.ApiResponse
import com.emines_transportation.util.validation.ValidationResult
import com.emines_transportation.util.validation.ValidationState

class PickupViewModel(private val repo: PickupRepo) : BaseViewModel() {
    private val transporterPickedUpOrderResponse =
        MutableLiveData<ApiResponse<CollectionBaseResponse<TransporterOrderResponse>>>()

    private val transporterInProcessOrderResponse =
        MutableLiveData<ApiResponse<CollectionBaseResponse<TransporterOrderResponse>>>()

    private val transporterDeliveredOrderResponse =
        MutableLiveData<ApiResponse<CollectionBaseResponse<TransporterOrderResponse>>>()


    private val changeOrderStatusResponse =
        MutableLiveData<ApiResponse<SuccessMsgResponse>>()

    private val pickedUpOrderResponse =
        MutableLiveData<ApiResponse<SuccessMsgResponse>>()

    private val deliveredOrderResponse =
        MutableLiveData<ApiResponse<SuccessMsgResponse>>()

    private val uploadDeliveredBillResponse =
        MutableLiveData<ApiResponse<SuccessMsgResponse>>()


    fun hitPickedUpOrderListApi(transporterId: Int, status: String) {
        repo.executeTransporterOrderApi(transporterId, status, transporterPickedUpOrderResponse)
    }

    fun getPickedUpOrderListResponse(): MutableLiveData<ApiResponse<CollectionBaseResponse<TransporterOrderResponse>>> {
        return transporterPickedUpOrderResponse
    }


    fun hitInProcessOrderListApi(transporterId: Int, status: String) {
        repo.executeTransporterOrderApi(transporterId, status, transporterInProcessOrderResponse)
    }

    fun getInProcessOrderListResponse(): MutableLiveData<ApiResponse<CollectionBaseResponse<TransporterOrderResponse>>> {
        return transporterInProcessOrderResponse
    }


    fun hitDeliveredOrderListApi(transporterId: Int, status: String) {
        repo.executeTransporterOrderApi(transporterId, status, transporterDeliveredOrderResponse)
    }

    fun getDeliveredOrderListResponse(): MutableLiveData<ApiResponse<CollectionBaseResponse<TransporterOrderResponse>>> {
        return transporterDeliveredOrderResponse
    }


    fun hitChangeOrderStatusByTransporterApi(transporterId: Int, orderId: Int, status: String) {
        repo.executeChangeOrderStatusByTransporterApi(
            transporterId,
            orderId,
            status,
            changeOrderStatusResponse
        )
    }

    fun getChangeOrderStatusByTransporterResponse(): MutableLiveData<ApiResponse<SuccessMsgResponse>> {
        return changeOrderStatusResponse
    }

    fun hitPickedUpOrderByTransporterApi(
        transporterId: Int,
        orderId: Int,
        weight: String,
        eWayNumber: String,
        uploadWeight: String?,
        uploadEWayBill: String?
    ) {
        repo.executePickedUpOrderByTransporterApi(
            transporterId,
            orderId,
            weight,
            eWayNumber,
            uploadWeight,
            uploadEWayBill,
            pickedUpOrderResponse
        )
    }

    fun getPickedUpOrderByTransporterResponse(): MutableLiveData<ApiResponse<SuccessMsgResponse>> {
        return pickedUpOrderResponse
    }


    fun hitDeliveredOrderByTransporterApi(
        transporterId: Int,
        orderId: Int,
        weight: String,
        grnNumber: String,
        deliveryWeightReceipt: String?,
        grnImage: String?
    ) {
        repo.executeDeliveredOrderByTransporterApi(
            transporterId,
            orderId,
            weight,
            grnNumber,
            deliveryWeightReceipt,
            grnImage,
            deliveredOrderResponse
        )
    }

    fun getDeliveredOrderByTransporterResponse(): MutableLiveData<ApiResponse<SuccessMsgResponse>> {
        return deliveredOrderResponse
    }


    var validationResponseObserver = MutableLiveData<ValidationState>()
    fun isValidData(buyerType: String?, fullName: String?, email: String?, phone: String?) {
        if (buyerType?.trim()
                ?.let { validator.validBuyerType(buyerType) } != ValidationResult.SUCCESS
        ) {
            if (buyerType?.trim()
                    ?.let { validator.validBuyerType(buyerType) } == ValidationResult.EMPTY_BUYER_TYPE
            ) {
                validationResponseObserver.postValue(
                    ValidationState(
                        ValidationResult.EMPTY_BUYER_TYPE,
                        R.string.error_buyer_type_empty
                    )
                )
                return
            }
        }
        if (fullName?.trim()
                ?.let { validator.validFullName(fullName) } != ValidationResult.SUCCESS
        ) {
            if (fullName?.trim()
                    ?.let { validator.validFullName(fullName) } == ValidationResult.EMPTY_FULL_NAME
            ) {
                validationResponseObserver.postValue(
                    ValidationState(
                        ValidationResult.EMPTY_FULL_NAME,
                        R.string.error_full_name_empty
                    )
                )
                return
            }
        }
        if (email?.trim()?.let { validator.validEmail(email) } != ValidationResult.SUCCESS) {
            if (email?.trim()
                    ?.let { validator.validEmail(email) } == ValidationResult.EMPTY_EMAIL
            ) {
                validationResponseObserver.postValue(
                    ValidationState(
                        ValidationResult.EMPTY_EMAIL,
                        R.string.error_email_empty
                    )
                )
                return
            }
            if (email?.trim()
                    ?.let { validator.validEmail(email) } == ValidationResult.INVALID_EMAIL
            ) {
                validationResponseObserver.postValue(
                    ValidationState(
                        ValidationResult.INVALID_EMAIL,
                        R.string.error_valid_email
                    )
                )
                return
            }

        }

        if (phone?.trim()
                ?.let { validator.validMobileNo(phone) } != ValidationResult.SUCCESS
        ) {
            if (phone?.trim()
                    ?.let { validator.validMobileNo(phone) } == ValidationResult.EMPTY_MOBILE_NUMBER
            ) {
                validationResponseObserver.postValue(
                    ValidationState(
                        ValidationResult.EMPTY_MOBILE_NUMBER,
                        R.string.error_mobile_empty
                    )
                )
                return
            }
            if (phone?.trim()
                    ?.let { validator.validMobileNo(phone) } == ValidationResult.VALID_MOBILE_NUMBER
            ) {
                validationResponseObserver.postValue(
                    ValidationState(
                        ValidationResult.VALID_MOBILE_NUMBER,
                        R.string.error_mobile_length
                    )
                )
                return
            }
        }


        validationResponseObserver.postValue(
            ValidationState(
                ValidationResult.SUCCESS,
                R.string.verify_success
            )
        )


    }


    /*
        fun getPickupList(): MutableList<Order> {
            val list = mutableListOf<Order>()
            list.add(Order(1,"","","","","","Pickup",""))
            list.add(Order(2,"","","","","","Pickup",""))
            list.add(Order(3,"","","","","","Denied",""))
            list.add(Order(4,"","","","","","Denied",""))
            list.add(Order(5,"","","","","","Pickup",""))
            return list
        }

        fun getInProcessList(): MutableList<Order> {
            val list = mutableListOf<Order>()
            list.add(Order(1,"","","","","","InProcess",""))
            list.add(Order(2,"","","","","","InProcess",""))
            list.add(Order(3,"","","","","","InProcess",""))
            return list
        }

        fun getDeliveryList(): MutableList<Order> {
            val list = mutableListOf<Order>()
            list.add(Order(1,"","","","","","Delivered",""))
            list.add(Order(2,"","","","","","Delivered",""))
            list.add(Order(3,"","","","","","Delivered",""))
            return list
        }
    */







    fun hitUploadDeliveredBillApi(
        transporterId: Int,
        orderId: Int,
        deliveryBillReceipt: String?,
    ) {
        repo.uploadDeliveredBill(
            transporterId,
            orderId,
            deliveryBillReceipt,
            uploadDeliveredBillResponse
        )
    }

    fun getUploadDeliveredBillResponse(): MutableLiveData<ApiResponse<SuccessMsgResponse>> {
        return uploadDeliveredBillResponse
    }

}