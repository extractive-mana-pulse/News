package com.example.newsapp.domain.repositoryImpl

import com.example.newsapp.data.remote.api.Api
import com.example.newsapp.data.remote.repository.Repository
import com.example.newsapp.domain.model.NewsResponse
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