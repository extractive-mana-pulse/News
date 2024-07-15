package com.example.newsapp.data.remote.api

import com.example.newsapp.domain.model.NewsResponse
import com.example.newsapp.presentation.activities.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("v2/top-headlines")
    suspend fun getAllNews(
        @Query("country")
        countryCode : String = "us",
        @Query("page")
        pageNumber : Int = 1,
        @Query("apiKey")
        apiKey : String = API_KEY
    ) : Response<NewsResponse>


    @GET("v2/everything")
    suspend fun searchAllNews(
        @Query("q")
        searchQuery : String,
        @Query("page")
        pageNumber : Int = 1,
        @Query("apiKey")
        apiKey : String = API_KEY
    ) : Response<NewsResponse>

}