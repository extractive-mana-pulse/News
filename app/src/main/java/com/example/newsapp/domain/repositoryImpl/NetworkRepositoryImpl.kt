package com.example.newsapp.domain.repositoryImpl

import com.example.newsapp.data.remote.Api
import com.example.newsapp.data.repository.Repository
import com.example.newsapp.domain.models.NewsResponse
import retrofit2.Response

class NetworkRepositoryImpl(
    private val api : Api
) : Repository {

    override suspend fun getAllNews(countryCode: String, pageNumber: Int): Response<NewsResponse> {
        return api.getAllNews(countryCode, pageNumber)
    }

    override suspend fun searchAllNews(searchQuery: String, pageNumber: Int): Response<NewsResponse> {
        return api.searchAllNews(searchQuery, pageNumber)
    }
}