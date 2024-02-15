package com.emines_transportation.model

import java.io.Serializable

data class UserAddress(
    var id: Long = -1,
    var type: String? = null,
    var country: String? = null,
    var state: String? = null,
    var city: String? = null,
    var area: String? = null,
    var piccode: String? = null,
    var address: String? = null,
    var isSelect: Boolean = false
) : Serializable