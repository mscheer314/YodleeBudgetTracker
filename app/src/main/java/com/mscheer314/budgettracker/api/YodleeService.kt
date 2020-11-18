package com.mscheer314.budgettracker.api

import com.mscheer314.budgettracker.data.Token
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface YodleeService {

    @Headers(
            "Api-Version: ${LoginInfo.API_VERSION}",
            "loginName: ${LoginInfo.LOGIN_NAME}",
    )

    @FormUrlEncoded
    @POST("auth/token")
    fun generateLoginToken(
            @Field("clientId") clientId: String,
            @Field("secret") secret: String
    ): Call<Token>
}