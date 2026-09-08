package com.example.travel_planner.data

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://ai-travel-planer-mgpb.onrender.com/api/v1/"

    private var tokenStore: TokenStore? = null

    fun init(context: Context) {
        tokenStore = TokenStore(context.applicationContext)
    }

    fun saveToken(token: String) {
        tokenStore?.saveToken(token)
    }

    fun clearToken() {
        tokenStore?.clearToken()
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // switch to NONE for release builds
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor { tokenStore?.getToken() })
            .addInterceptor(loggingInterceptor)
            .connectTimeout(150, TimeUnit.SECONDS) // <-- increased again: confirmed cold-start + OTP email dispatch can take ~2 min
            .readTimeout(150, TimeUnit.SECONDS)
            .writeTimeout(150, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApi by lazy { retrofit.create(AuthApi::class.java) }
    val apiService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}

private class AuthInterceptor(private val tokenProvider: () -> String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = tokenProvider()
        val request = if (token != null) {
            original.newBuilder().addHeader("Authorization", "Bearer $token").build()
        } else {
            original
        }
        return chain.proceed(request)
    }
}