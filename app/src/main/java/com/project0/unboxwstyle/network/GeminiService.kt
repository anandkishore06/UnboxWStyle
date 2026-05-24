package com.project0.unboxwstyle.network

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

object GeminiService {

    private val client = OkHttpClient()

    fun generateRecommendation(

        apiKey: String,

        prompt: String,

        callback: (String) -> Unit
    ) {

        val json = JSONObject()

        val contentsArray = JSONArray()

        val contentObject = JSONObject()

        val partsArray = JSONArray()

        val textObject = JSONObject()

        textObject.put(
            "text",
            prompt
        )

        partsArray.put(textObject)

        contentObject.put(
            "parts",
            partsArray
        )

        contentsArray.put(contentObject)

        json.put(
            "contents",
            contentsArray
        )

        val body = RequestBody.create(

            "application/json".toMediaType(),

            json.toString()
        )

        val request = Request.Builder()

            .url(
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.0-pro:generateContent?key=$apiKey"
            )

            .post(body)

            .addHeader(
                "Content-Type",
                "application/json"
            )

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

                        val jsonResponse =
                            JSONObject(responseBody!!)

                        val text =

                            jsonResponse

                                .getJSONArray("candidates")

                                .getJSONObject(0)

                                .getJSONObject("content")

                                .getJSONArray("parts")

                                .getJSONObject(0)

                                .getString("text")

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