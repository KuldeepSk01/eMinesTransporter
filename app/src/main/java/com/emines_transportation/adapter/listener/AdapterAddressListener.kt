package com.emines_transportation.adapter.listener

import com.emines_transportation.model.UserAddress

interface AdapterAddressListener {
    fun onAddressSelect(address: UserAddress)
    fun onEditAddress(address: UserAddress)
}