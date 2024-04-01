package com.example.newsapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.models.Articles

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(articles : Articles) : Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Articles>>

    @Delete
    suspend fun deleteArticle(articles: Articles)

}