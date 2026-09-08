package com.example.travel_planner.data

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/register")
    suspend fun register(@Body request: com.example.travel_planner.data.RegisterRequest): ApiResponse<RegisterData>

    @POST("auth/verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): ApiResponse<Unit?>

    @POST("auth/resend-otp")
    suspend fun resendOtp(@Body request: ResendOtpRequest): ApiResponse<Unit?>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<LoginData>
}


