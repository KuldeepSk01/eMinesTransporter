package com.emines_transportation.util

object Constants {
    class RequestCodes {
        companion object {
            const val CAMERA_CODE = 100
            const val IMAGE_PICK_CODE = 101
        }
    }

    class UrlsEndPoint {
        companion object {
            const val login = "transporterLogin"
            const val verifyOtp = "transporterVerifyOtp"

            const val transporterOrders = "transporterOrders"
            const val orderInfoInTransporter = "orderInfoInTransporter"
            const val changeOrderStatusByTransporter = "changeOrderStatuByTransporter"
            const val pickedUpByTransporter = "pickedUpByTransporter"
            const val deliverByTransporter = "deliverByTransporter"

            const val editProfile = "edit_profile"
            const val transporter_dashboard = "transporter_dashboard"

            const val uploadDeliveryBill = "uploadDeliveryBill"

        }
    }

    class NetworkConstant {
        companion object {
            const val API_SUCCESS = 200
            const val API_AUTH_ERROR = 401
            const val API_TIMEOUT = 60000L
            const val NO_INTERNET_AVAILABLE = "Oops! No Internet"
            const val CONNECTION_LOST = "Oops! Connection lost! "
            const val BASE_URL = "https://emines.co/api/"
        }
    }

    class PreferenceConstant {
        companion object {
            const val PREFERENCE_NAME = "emines_transportation"
            const val IS_LOGIN = "IS_LOGIN"
            const val PRESENT = "1"
            const val IS_EMAIL_OR_MOBILE_INVALID = "IS_EMAIL_OR_MOBILE_INVALID"
            const val IS_EMAIL_OR_MOBILE_VALID = "IS_EMAIL_OR_MOBILE_INVALID"
            const val IS_PRESENT = "IS_PRESENT"
            const val IS_CURRENT_DATE = "IS_CURRENT_DATE"
            const val TOKEN = "TOKEN"
            const val AUTHORIZATION = "authorization"
            const val USER_DETAIL = "USER_DETAIL"

            const val C_NAME = "C_NAME"
            const val C_PAN = "C_PAN"
            const val C_PAN_FILE = "C_PAN_FILE"
            const val C_GST = "C_GST"
            const val C_GST_FILE = "C_GST_FILE"
        }
    }

    class DefaultConstant {
        companion object {
            const val BUNDLE_KEY = "BUNDLE_KEY"
            const val STRING_KEY = "STRING_KEY"
            const val MODEL_DETAIL = "MODEL_DETAIL"
            const val OTP_DETAIL = "OTP_DETAIL"
            const val LOCATION_PERMISSION_ERROR = "Please enable location permission"

            const val TRANSPORTER_IMAGE = "https://cdn-icons-png.flaticon.com/512/1535/1535791.png"
            const val DRIVER_ORDER_IMAGE = "https://cdn-icons-png.flaticon.com/512/5465/5465975.png"
        }
    }


}