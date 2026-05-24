package com.project0.unboxwstyle.network

import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

object OpenAIService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .build()

    fun generateChatResponse(
        apiKey: String,
        messagesList: List<com.project0.unboxwstyle.model.ChatMessage>,
        wardrobeContext: String = "",
        callback: (String) -> Unit
    ) {
        val json = JSONObject()
        json.put("model", "gpt-4o-mini")

        val messages = JSONArray()
        
        // AGGRESSIVE SYSTEM INSTRUCTION
        val systemMessage = JSONObject()
        systemMessage.put("role", "system")
        
        val contextPrompt = if (wardrobeContext.isNotBlank() && !wardrobeContext.contains("empty", ignoreCase = true)) {
            """
            You are the user's personal AI Fashion Stylist. 
            
            CRITICAL ACCESS: You have DIRECT ACCESS to the user's FULL wardrobe data.
            USER WARDROBE CONTENT (Item Category and Image URL):
            $wardrobeContext
            
            INSTRUCTIONS:
            1. You MUST acknowledge the specific items and their descriptions listed above.
            2. NEVER say "I cannot see your wardrobe" or "I don't have access".
            3. When suggesting outfits, choose from the SPECIFIC items in the list above.
            4. If you recommend an item, YOU MUST include its full Image URL in your response like this: [IMAGE_URL].
            5. Provide a stylish explanation for your choice. 
            6. DO NOT use markdown formatting like double asterisks (**) for bolding. Use plain text only.
            """.trimIndent()
        } else {
            "You are a personal fashion stylist. The user's wardrobe is currently empty in the database. Instruct them to go to the 'Upload' screen to add their clothes so you can see them."
        }

        systemMessage.put("content", contextPrompt)
        messages.put(systemMessage)

        messagesList.forEach { msg ->
            if (!msg.text.startsWith("CONTEXT:")) { // Don't include the raw context as a chat bubble
                val messageObj = JSONObject()
                messageObj.put("role", if (msg.isUser) "user" else "assistant")
                messageObj.put("content", msg.text)
                messages.put(messageObj)
            }
        }

        json.put("messages", messages)

        val body = json.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                callback("Error: ${e.localizedMessage}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val responseBody = response.body?.string()
                if (!response.isSuccessful) {
                    callback("API Error")
                    return
                }
                try {
                    val jsonResponse = JSONObject(responseBody!!)
                    val text = jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content")
                    callback(text)
                } catch (e: Exception) {
                    callback("Parsing error")
                }
            }
        })
    }

    fun generateRecommendation(

        apiKey: String,

        prompt: String,

        callback: (String) -> Unit
    ) {

        val json = JSONObject()

        json.put(
            "model",
            "gpt-4o-mini"
        )

        val messages = JSONArray()

        val userMessage = JSONObject()

        userMessage.put(
            "role",
            "user"
        )

        userMessage.put(
            "content",
            prompt
        )

        messages.put(userMessage)

        json.put(
            "messages",
            messages
        )

        val body =

            json.toString()

                .toRequestBody(
                    "application/json"
                        .toMediaType()
                )

        val request = Request.Builder()

            .url(
                "https://api.openai.com/v1/chat/completions"
            )

            .addHeader(
                "Authorization",
                "Bearer $apiKey"
            )

            .post(body)

            .build()

        client.newCall(request)
            .enqueue(object : okhttp3.Callback {

                override fun onFailure(
                    call: okhttp3.Call,
                    e: IOException
                ) {
                    Log.e("OpenAIService", "Request failed", e)
                    callback("Network error: ${e.localizedMessage}")
                }

                override fun onResponse(
                    call: okhttp3.Call,
                    response: okhttp3.Response
                ) {
                    val responseBody = response.body?.string()
                    
                    if (!response.isSuccessful) {
                        Log.e("OpenAIService", "Error response: $responseBody")
                        callback("API Error: $responseBody")
                        return
                    }

                    try {
                        val jsonResponse = JSONObject(responseBody!!)
                        val text = jsonResponse
                                .getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content")
                        callback(text)
                    } catch (e: Exception) {
                        Log.e("OpenAIService", "Parsing failed", e)
                        callback("Parsing error: $responseBody")
                    }
                }
            })
    }
}