package com.emines_transportation.network

class ApiResponse<T>(val status: Status, val data: T?, val error: Throwable?) {

    companion object {

        fun <T> loading(): ApiResponse<T> {
            return ApiResponse(Status.LOADING, null, null)
        }

        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(Status.SUCCESS, data, null)
        }

        fun <T> error(error: Throwable): ApiResponse<T> {
            return ApiResponse(Status.ERROR, null, error)
        }
    }

    enum class Status {
        LOADING, SUCCESS, ERROR
    }

    enum class ERROR {
        API_ERROR, NO_INTERNET_ERROR, SERVER_ERROR, TIMEOUT_ERROR
    }
}