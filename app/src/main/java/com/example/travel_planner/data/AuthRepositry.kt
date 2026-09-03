package com.example.travel_planner.data



object AuthRepository {

    private val api = RetrofitClient.authApi

    suspend fun emailExists(email: String): Result<Boolean> = runCatching {
        api.checkEmail(CheckEmailRequest(email)).exists
    }

    suspend fun signUp(name: String, email: String, password: String): Result<SignUpResponse> = runCatching {
        api.signUp(SignUpRequest(name, email, password))
    }

    suspend fun login(email: String, password: String): Result<LoginResponse> = runCatching {
        val response = api.login(LoginRequest(email, password))
        RetrofitClient.saveToken(response.token) // <-- added
        response
    }

    suspend fun sendOtp(email: String): Result<Unit> = runCatching {
        api.sendOtp(SendOtpRequest(email))
        Unit
    }

    suspend fun verifyOtp(email: String, code: String): Result<LoginResponse> = runCatching {
        val response = api.verifyOtp(VerifyOtpRequest(email, code))
        RetrofitClient.saveToken(response.token) // <-- added
        response
    }

    fun logout() {
        RetrofitClient.clearToken()
    }
}