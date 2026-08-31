package com.example.travel_planner.data

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/check-email")
    suspend fun checkEmail(@Body request: CheckEmailRequest): CheckEmailResponse

    @POST("auth/signup")
    suspend fun signUp(@Body request: SignUpRequest): SignUpResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/send-otp")
    suspend fun sendOtp(@Body request: SendOtpRequest): GenericResponse

    @POST("auth/verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): LoginResponse
}

data class CheckEmailRequest(val email: String)
data class CheckEmailResponse(val exists: Boolean)

data class SignUpRequest(val name: String, val email: String, val password: String)
data class SignUpResponse(val userId: String, val otpSent: Boolean)

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String, val userId: String, val name: String, val email: String)

data class SendOtpRequest(val email: String)
data class VerifyOtpRequest(val email: String, val code: String)

data class GenericResponse(val success: Boolean, val message: String? = null)