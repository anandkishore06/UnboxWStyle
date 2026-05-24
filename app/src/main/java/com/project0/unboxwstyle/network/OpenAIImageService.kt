package com.project0.unboxwstyle.network

import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

object OpenAIImageService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .build()

    fun generateImage(
        apiKey: String,
        prompt: String,
        callback: (String) -> Unit
    ) {
        if (apiKey.isBlank()) {
            Log.e("OpenAIImage", "API Key is empty!")
            callback("")
            return
        }

        // Primary: dall-e-3
        tryModel(apiKey, "dall-e-3", prompt) { urlD3 ->
            if (urlD3.isNotEmpty()) {
                callback(urlD3)
            } else {
                Log.e("OpenAIImage", "DALL-E 3 failed, attempting DALL-E 2")
                tryModel(apiKey, "dall-e-2", prompt) { urlD2 ->
                    callback(urlD2)
                }
            }
        }
    }

    private fun tryModel(
        apiKey: String,
        modelName: String,
        prompt: String,
        onResult: (String) -> Unit
    ) {
        val json = JSONObject()
        json.put("model", modelName)
        json.put("prompt", prompt)
        json.put("n", 1)
        json.put("size", "1024x1024")

        val body = json.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("https://api.openai.com/v1/images/generations")
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build()

        Log.d("OpenAIImage", "Attempting model: $modelName")

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("OpenAIImage", "$modelName network failure: ${e.message}")
                onResult("")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val responseBody = response.body?.string()
                Log.d("OpenAIImage", "$modelName HTTP Code: ${response.code}")

                if (!response.isSuccessful) {
                    Log.e("OpenAIImage", "$modelName error: $responseBody")
                    onResult("")
                    return
                }

                try {
                    val jsonResponse = JSONObject(responseBody!!)
                    val imageUrl = jsonResponse.getJSONArray("data").getJSONObject(0).getString("url")
                    Log.d("OpenAIImage", "$modelName success! URL: $imageUrl")
                    onResult(imageUrl)
                } catch (e: Exception) {
                    Log.e("OpenAIImage", "$modelName parsing failed", e)
                    onResult("")
                }
            }
        })
    }
}
