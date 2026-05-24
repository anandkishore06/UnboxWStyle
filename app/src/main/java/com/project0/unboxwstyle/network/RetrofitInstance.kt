package com.project0.unboxwstyle.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: GeminiApi by lazy {

        Retrofit.Builder()

            .baseUrl(
                "https://generativelanguage.googleapis.com/"
            )

            .addConverterFactory(
                GsonConverterFactory.create()
            )

            .build()

            .create(GeminiApi::class.java)
    }
}