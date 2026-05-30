package com.securityguard.app.ui.navigation

object Routes {
    const val LOGIN = "login"
    const val OTP = "otp/{employeeNumber}"
    const val FORGOT_STEP1 = "forgot/step1"
    const val FORGOT_STEP2 = "forgot/step2/{employeeNumber}"
    const val FORGOT_STEP3 = "forgot/step3/{employeeNumber}/{otp}"
    const val GUARD_HOME = "guard"
    const val INWARD_DETAIL = "inward/{entryId}"
    const val OUTWARD_DETAIL = "outward/{entryId}"
    const val VISITOR_DETAIL = "visitor/{entryId}"

    fun otp(employeeNumber: String) = "otp/$employeeNumber"
    fun forgotStep2(employeeNumber: String) = "forgot/step2/$employeeNumber"
    fun forgotStep3(employeeNumber: String, otp: String) = "forgot/step3/$employeeNumber/$otp"
    fun inwardDetail(id: String) = "inward/$id"
    fun outwardDetail(id: String) = "outward/$id"
    fun visitorDetail(id: String) = "visitor/$id"
}
