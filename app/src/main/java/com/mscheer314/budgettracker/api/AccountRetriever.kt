package com.mscheer314.budgettracker.api

import com.mscheer314.budgettracker.data.Token
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AccountRetriever {
    private val service: YodleeService

    companion object {
        const val BASE_URL = "https://sandbox.api.yodlee.com:443/ysl/"
    }

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpBuilder = OkHttpClient.Builder()
        httpBuilder
                .addInterceptor(interceptor)
        val client = httpBuilder.build()

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        service = retrofit.create(YodleeService::class.java)
    }

    fun login(callback: Callback<Token>) {
        val call =
                service.generateLoginToken(LoginInfo.CLIENT_ID, LoginInfo.SECRET)
        call.enqueue(callback)
    }
}