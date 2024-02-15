package com.emines_transportation.model

import java.io.Serializable

data class Order(
    var id: Long = -1,
    var orderType: String? = null,
    var weight: String? = null,
    var rate: String? = null,
    var estPrice: String? = null,
    var date: String? = null,
    var status: String? = null,
    var orderReq: String? = null
) : Serializable