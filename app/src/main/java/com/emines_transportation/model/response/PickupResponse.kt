package com.emines_transportation.model.response

import java.io.Serializable

data class PickupResponse(
    val aadhar_file: String,
    val aadhar_no: String,
    val aadhar_status: String,
    val account: String,
    val account_finance_email: String,
    val account_finance_name: String,
    val account_finance_telephone: String,
    val account_no: String,
    val account_type: String,
    val added_by_user_id: String,
    val approveflag: String,
    val bank_account_name: String,
    val bank_account_number: String,
    val bank_name: String,
    val beneficiary: String,
    val branch: String,
    val buyer_type: String,
    val cancle_cheque: String,
    val cancle_cheque_file: String,
    val cancle_cheque_status: String,
    val company_email: String,
    val company_logo: String,
    val company_name: String,
    val company_phone: String,
    val company_status: String,
    val company_type: String,
    val contact_email: String,
    val contact_phone: String,
    val created_at: String,
    val currency: String,
    val delivery_mode: String,
    val director_email: String,
    val director_name: String,
    val director_telephone: String,
    val displayflag: String,
    val email: String,
    val environment_certificate: String,
    val environment_certificate_status: String,
    val financial_address: String,
    val gst_file: String,
    val gst_no: String,
    val gst_status: String,
    val id: Int,
    val ifsc_code: String,
    val management_email: String,
    val management_name: String,
    val management_telephone: String,
    val mt: String,
    val mt_value: String,
    val name: String,
    val no_of_employee: String,
    val number_of_directors: String,
    val pan_file: String,
    val pan_no: String,
    val pan_status: String,
    val payment_method: String,
    val payment_terms: String,
    val phone: String,
    val profile_pic: String,
    val rtgs: String,
    val sales_enquiry_email: String,
    val sales_enquiry_name: String,
    val sales_enquiry_telephone: String,
    val target_rs: String,
    val target_unit: String,
    val telephone: String,
    val updated_at: String,
    val upi_number: String,
    val website: String,
    val buying_requests: Int,
    val purchased_orders: Int,
    val total_orders: Int,
    var isEditBuyer: Boolean = false
) : Serializable