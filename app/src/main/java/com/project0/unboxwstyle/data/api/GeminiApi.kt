package com.project0.unboxwstyle.network

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

data class GeminiRequest(

    val contents: List<Content>
)

data class Content(

    val parts: List<Part>
)

data class Part(

    val text: String
)

data class GeminiResponse(

    val candidates: List<Candidate>
)

data class Candidate(

    val content: Content
)

interface GeminiApi {

    @POST("v1beta/models/gemini-2.0-flash:generateContent")

    suspend fun generateRecommendation(

        @Query("key")
        apiKey: String,

        @Body
        request: GeminiRequest
    ): GeminiResponse
}