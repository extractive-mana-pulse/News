package com.example.newsapp.domain.repositoryImpl

import androidx.lifecycle.LiveData
import com.example.newsapp.data.local.ArticleDatabase
import com.example.newsapp.data.repository.DbRepository
import com.example.newsapp.domain.models.Articles

class DbRepositoryImpl(
    private val db : ArticleDatabase
) : DbRepository {
    override suspend fun insert(articles: Articles): Long = db.getArticleDao().insert(articles)

    override fun getAllArticles(): LiveData<List<Articles>> = db.getArticleDao().getAllArticles()

    override suspend fun deleteArticle(articles: Articles) = db.getArticleDao().deleteArticle(articles)

}