package com.emines_transportation.util.validation

/**
 * Validator class contains validation methods for application
 **/
class Validator(private val validationHelper: ValidationHelper) {
    fun validEmail(email: String): ValidationResult {
        return if (!validationHelper.isEmptyField(email)) {
            if (validationHelper.isValidEmail(email)) return ValidationResult.SUCCESS
            else return ValidationResult.INVALID_EMAIL
        } else {
            return ValidationResult.EMPTY_EMAIL
        }
    }


    fun validMobileNo(mobile: String): ValidationResult {
        return if (!validationHelper.isEmptyField(mobile)) {
            if (mobile.trim().length < 10) {
                return ValidationResult.VALID_MOBILE_NUMBER
            } else {
                return ValidationResult.SUCCESS
            }
        } else {
            return ValidationResult.EMPTY_MOBILE_NUMBER
        }
    }

    fun validFullName(text: String): ValidationResult {
        return if (!validationHelper.isEmptyField(text)) {
            return ValidationResult.SUCCESS
        } else {
            return ValidationResult.EMPTY_FULL_NAME
        }
    }

    fun validBuyerType(text: String): ValidationResult {
        return if (!validationHelper.isEmptyField(text)) {
            return ValidationResult.SUCCESS
        } else {
            return ValidationResult.EMPTY_BUYER_TYPE
        }
    }
}