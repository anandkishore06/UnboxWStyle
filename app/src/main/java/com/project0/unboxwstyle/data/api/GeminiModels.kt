package com.project0.unboxwstyle.data.api

import com.google.gson.annotations.SerializedName

//data class GeminiRequest(
//    @SerializedName("contents")
//    val contents: List<Content>
//)

//data class Content(
//    @SerializedName("parts")
//    val parts: List<Part>
//)

//data class Part(
//    @SerializedName("text")
//    val text: String
//)

//data class GeminiResponse(
//    @SerializedName("candidates")
//    val candidates: List<Candidate>? = null,
//    @SerializedName("usageMetadata")
//    val usageMetadata: UsageMetadata? = null
//)

//data class Candidate(
//    @SerializedName("content")
//    val content: Content? = null,
//    @SerializedName("finishReason")
//    val finishReason: String? = null,
//    @SerializedName("index")
//    val index: Int? = null,
//    @SerializedName("safetyRatings")
//    val safetyRatings: List<SafetyRating>? = null
//)

data class SafetyRating(
    @SerializedName("category")
    val category: String,
    @SerializedName("probability")
    val probability: String
)

data class UsageMetadata(
    @SerializedName("promptTokenCount")
    val promptTokenCount: Int,
    @SerializedName("candidatesTokenCount")
    val candidatesTokenCount: Int,
    @SerializedName("totalTokenCount")
    val totalTokenCount: Int
)
