package com.example.travel_planner.data

object AuthRepository {

    private val api = RetrofitClient.authApi

    suspend fun signUp(fullName: String, email: String, password: String): Result<RegisterData> = runCatching {
        val response = api.register(RegisterRequest(fullName = fullName, email = email, password = password))
        if (!response.success) throw Exception(response.message)
        response.data ?: throw Exception("No data in register response")
    }

    suspend fun login(email: String, password: String): Result<LoginData> = runCatching {
        val response = api.login(LoginRequest(email = email, password = password))
        if (!response.success) throw Exception(response.message)
        val data = response.data ?: throw Exception("No data in login response")
        RetrofitClient.saveToken(data.accessToken)
        data
    }

    suspend fun sendOtp(email: String): Result<Unit> = runCatching {
        val response = api.resendOtp(ResendOtpRequest(email = email))
        if (!response.success) throw Exception(response.message)
        Unit
    }

    suspend fun verifyOtp(email: String, code: String): Result<Unit> = runCatching {
        val response = api.verifyOtp(VerifyOtpRequest(email = email, otp = code))
        if (!response.success) throw Exception(response.message)
        Unit
    }

    fun logout() {
        RetrofitClient.clearToken()
    }
}