package com.example.travel_planner.data

import com.example.travel_planner.data.CheckEmailRequest
import com.example.travel_planner.data.LoginRequest
import com.example.travel_planner.data.LoginResponse
import com.example.travel_planner.data.RetrofitClient
import com.example.travel_planner.data.SendOtpRequest
import com.example.travel_planner.data.SignUpRequest
import com.example.travel_planner.data.SignUpResponse
import com.example.travel_planner.data.VerifyOtpRequest

object AuthRepository {

    private val api = RetrofitClient.authApi

    suspend fun emailExists(email: String): Result<Boolean> = runCatching {
        api.checkEmail(CheckEmailRequest(email)).exists
    }

    suspend fun signUp(name: String, email: String, password: String): Result<SignUpResponse> = runCatching {
        api.signUp(SignUpRequest(name, email, password))
    }

    suspend fun login(email: String, password: String): Result<LoginResponse> = runCatching {
        api.login(LoginRequest(email, password))
    }

    suspend fun sendOtp(email: String): Result<Unit> = runCatching {
        api.sendOtp(SendOtpRequest(email))
        Unit
    }

    suspend fun verifyOtp(email: String, code: String): Result<LoginResponse> = runCatching {
        api.verifyOtp(VerifyOtpRequest(email, code))
    }
}