package com.example.newsapp.data.repository

import com.example.newsapp.domain.models.NewsResponse
import retrofit2.Response

interface Repository {

    suspend fun getAllNews(countryCode : String, pageNumber : Int) : Response<NewsResponse>

    suspend fun searchAllNews(searchQuery : String, pageNumber : Int = 1) : Response<NewsResponse>

}
