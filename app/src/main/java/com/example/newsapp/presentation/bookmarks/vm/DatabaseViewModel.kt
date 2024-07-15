package com.example.newsapp.presentation.bookmarks.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.local.repository.DbRepository
import com.example.newsapp.domain.model.Articles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel @Inject constructor(
    private val repository: DbRepository
): ViewModel() {

    private val _stateFlow = MutableStateFlow<ButtonState>(ButtonState.Static)
    val stateFlow = _stateFlow.asStateFlow()

    fun saveArticle(articles: Articles) = viewModelScope.launch {
        repository.insert(articles)
        buttonClicked()
    }

    private fun buttonClicked() {
        _stateFlow.value = ButtonState.Added
    }

    fun getSavedNews() = repository.getAllArticles()

    fun deleteArticle(articles: Articles) = viewModelScope.launch {
        repository.deleteArticle(articles)
    }
}

sealed class ButtonState{
    data object Static: ButtonState()
    data object Added: ButtonState()
}