package com.emines_transportation.util.validation

import android.text.TextUtils
import android.util.Patterns

/**
 * validationHelper is helper class for validator
 * */
class ValidationHelper {

    fun isEmptyField(text: String): Boolean {
        return TextUtils.isEmpty(text)
    }

    fun isValidEmail(email: String): Boolean {
        val p = Patterns.EMAIL_ADDRESS
        val m = p.matcher(email)
        return m.matches() && email.trim { it >= ' ' }.isEmpty()
    }

}