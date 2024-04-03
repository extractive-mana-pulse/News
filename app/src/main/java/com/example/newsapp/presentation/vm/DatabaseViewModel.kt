package com.example.newsapp.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.repository.DbRepository
import com.example.newsapp.domain.models.Articles
import com.example.newsapp.domain.repositoryImpl.DbRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel @Inject constructor(
    private val repository: DbRepository
): ViewModel() {

    fun saveArticle(articles: Articles) = viewModelScope.launch {
        repository.insert(articles)
    }

    fun getSavedNews() = repository.getAllArticles()

    fun deleteArticle(articles: Articles) = viewModelScope.launch {
        repository.deleteArticle(articles)
    }

}