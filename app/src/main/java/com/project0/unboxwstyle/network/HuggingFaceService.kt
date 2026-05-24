package com.project0.unboxwstyle.network

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

object HuggingFaceService {

    private val client = OkHttpClient()

    fun generateRecommendation(

        apiKey: String,

        prompt: String,

        callback: (String) -> Unit
    ) {

        val json = JSONObject()

        json.put(
            "inputs",
            prompt
        )

        val body = RequestBody.create(

            "application/json".toMediaType(),

            json.toString()
        )

        val request = Request.Builder()

            .url(
                "https://api-inference.huggingface.co/models/mistralai/Mistral-7B-Instruct-v0.2"
            )

            .addHeader(
                "Authorization",
                "Bearer $apiKey"
            )

            .post(body)

            .build()

        client.newCall(request)
            .enqueue(object : Callback {

                override fun onFailure(
                    call: Call,
                    e: IOException
                ) {

                    callback(
                        e.localizedMessage
                            ?: "Error"
                    )
                }

                override fun onResponse(
                    call: Call,
                    response: Response
                ) {

                    val responseBody =
                        response.body?.string()

                    try {

                        val jsonArray =
                            JSONArray(responseBody)

                        val text =

                            jsonArray

                                .getJSONObject(0)

                                .getString(
                                    "generated_text"
                                )

                        callback(text)

                    } catch (e: Exception) {

                        callback(
                            responseBody
                                ?: "Parsing error"
                        )
                    }
                }
            })
    }
}