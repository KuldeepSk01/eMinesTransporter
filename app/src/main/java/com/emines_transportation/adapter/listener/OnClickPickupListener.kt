package com.emines_transportation.adapter.listener

import com.emines_transportation.model.response.TransporterOrderResponse

interface OnClickPickupListener {

    fun onClickTotalOrder(model: TransporterOrderResponse)
}