package com.emines_transportation.model.response

data class TransporterOrderDetailResponse(
    val address: String,
    val address_type: String,
    val approve_date: String,
    val area: String,
    val buyer_id: Int,
    val city: String,
    val country: String,
    val created_at: String,
    val delivery_address_id: String,
    val despatch_through: String,
    val destination: String,
    val enter_gate_no: String,
    val four_product_gst: String,
    val four_purchased_quantity: String,
    val four_requested_product: String,
    val four_requested_product_id: String,
    val four_terms_code: String,
    val four_total_unit_price: String,
    val four_total_unit_price_excl_gst: String,
    val four_total_unit_price_incl_gst: String,
    val four_unit_of_quantity: String,
    val four_unit_rate: String,
    val id: Int,
    val indent_no: String,
    val invoice_date: String,
    val invoice_no: String,
    val invoice_status: String,
    val notes: String,
    val one_product_gst: String,
    val one_purchased_quantity: String,
    val one_requested_product: String,
    val one_requested_product_id: String,
    val one_terms_code: String,
    val one_total_unit_price: String,
    val one_total_unit_price_excl_gst: String,
    val one_total_unit_price_incl_gst: String,
    val one_unit_of_quantity: String,
    val one_unit_rate: String,
    val order_no: String,
    val parent_order: Int,
    val pincode: String,
    val po_date: String,
    val po_no: String,
    val po_value: String,
    val state: String,
    val status: String,
    val three_product_gst: String,
    val three_purchased_quantity: String,
    val three_requested_product: String,
    val three_requested_product_id: String,
    val three_terms_code: String,
    val three_total_unit_price: String,
    val three_total_unit_price_excl_gst: String,
    val three_total_unit_price_incl_gst: String,
    val three_unit_of_quantity: String,
    val three_unit_rate: String,
    val total_amount: Int,
    val total_amount_with_gst: String,
    val total_amount_without_gst: String,
    val total_order_category: String,
    val total_purchased_quantity: String,
    val total_rate: Int,
    val transporter: String,
    val two_product_gst: String,
    val two_purchased_quantity: String,
    val two_requested_product: String,
    val two_requested_product_id: String,
    val two_terms_code: String,
    val two_total_unit_price: String,
    val two_total_unit_price_excl_gst: String,
    val two_total_unit_price_incl_gst: String,
    val two_unit_of_quantity: String,
    val two_unit_rate: String,
    val updated_at: String,
    val upload_po: String,
    val voucher_no: String
)