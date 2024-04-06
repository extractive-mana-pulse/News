package com.example.newsapp.presentation.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.repository.Repository
import com.example.newsapp.domain.models.NewsResponse
import com.example.newsapp.presentation.sealed.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository : Repository
) : ViewModel() {

    val tryResponse: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var pageNumber = 1
    var breakingNewsResponse : NewsResponse? = null

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {

        viewModelScope.launch {
            delay(3000)
            _isLoading.value = false
        }

        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode : String) = viewModelScope.launch {
        tryResponse.postValue(Resource.Loading())
        val response = repository.getAllNews(countryCode, pageNumber)
        tryResponse.postValue(handleBreakingNews(response))
    }

    private fun handleBreakingNews(response : Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                pageNumber++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = result
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = result.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: result)
            }
        }
        return Resource.Error(response.code().toString())
    }
}