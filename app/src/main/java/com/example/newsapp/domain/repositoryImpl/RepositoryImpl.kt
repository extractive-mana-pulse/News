package com.example.newsapp.domain.repositoryImpl

import com.example.newsapp.models.NewsResponse
import com.example.newsapp.data.remote.Api
import com.example.newsapp.domain.repository.Repository
import retrofit2.Response

class RepositoryImpl(
    private val api : Api
) : Repository {

    override suspend fun getAllNews(countryCode: String, pageNumber: Int): Response<NewsResponse> {
        return api.getAllNews(countryCode, pageNumber)
    }

    override suspend fun searchAllNews(searchQuery: String, pageNumber: Int): Response<NewsResponse> {
        return api.searchAllNews(searchQuery, pageNumber)
    }
}