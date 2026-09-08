package com.example.travel_planner.data

import com.google.gson.annotations.SerializedName


data class RegisterRequest(
    @SerializedName("fullName") val fullName: String, // <-- was "name" — real field is "fullName"
    @SerializedName("email") val email: String? = null,
    @SerializedName("phoneNumber") val phoneNumber: String? = null,
    @SerializedName("password") val password: String
)

data class RegisterData(
    @SerializedName("userId") val userId: String,
    @SerializedName("email") val email: String?,
    @SerializedName("phoneNumber") val phoneNumber: String?
)
data class VerifyOtpRequest(
    @SerializedName("email") val email: String? = null,
    @SerializedName("phoneNumber") val phoneNumber: String? = null,
    @SerializedName("otp") val otp: String
)

data class ResendOtpRequest(
    @SerializedName("email") val email: String? = null,
    @SerializedName("phoneNumber") val phoneNumber: String? = null
)

data class LoginRequest(
    @SerializedName("email") val email: String? = null,
    @SerializedName("phoneNumber") val phoneNumber: String? = null,
    @SerializedName("password") val password: String
)

data class AuthUser(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("fullName") val fullName: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phoneNumber") val phoneNumber: String? = null
)

data class LoginData(
    @SerializedName("user") val user: AuthUser,
    @SerializedName("accessToken") val accessToken: String, // <-- was "token" — real field is "accessToken"
    @SerializedName("refreshToken") val refreshToken: String? = null
)