package com.example.aijournalcompanion

import retrofit2.http.Body
import retrofit2.http.POST

// send to backend
data class AnalyzeRequest(
    val content: String
)
//receive from backend
data class AnalyzeResponse(
    val emotion: String,
    val advice: String
)

//api interface
interface ApiService {
    @POST("analyze")
    suspend fun analyzeJournal(@Body request: AnalyzeRequest):AnalyzeResponse
}