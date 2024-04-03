package com.example.newsapp.data.repository

import androidx.lifecycle.LiveData
import com.example.newsapp.domain.models.Articles

interface DbRepository {

    suspend fun insert(articles : Articles) : Long

    fun getAllArticles(): LiveData<List<Articles>>

    suspend fun deleteArticle(articles: Articles)

}