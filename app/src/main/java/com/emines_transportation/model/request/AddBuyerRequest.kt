package com.emines_transportation.model.request

import java.io.Serializable

data class AddBuyerRequest(
    var userId: Int = 0,
    var buyer_type: String? = null,
    var full_name: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var company_name: String? = null,
    var company_email: String? = null,
    var website: String? = null,
    var company_phone: String? = null,
    var account_no: String? = null,
    var beneficiary: String? = null,
    var bank_name: String? = null,
    var ifsc_code: String? = null,
    var pan_no: String? = null,
    var gst_no: String? = null,
    var aadhar_no: String? = null,
    var pan_file_path: String? = null,
    var gst_file_path: String? = null,
    var aadhar_file_path: String? = null,

    var environment_file_path: String? = null,
    var cancelled_cheque_file_path: String? = null,
    var environment_certificate_number: String? = null,
    var cancelled_cheque_certificate_number: String? = null,
    var numberOfDirector: String? = null,
    var numberOfEmployee: String? = null,
    var companyStatus: String? = null,
    var mt: String? = null,
    var value: String? = null,

    ) : Serializable
