package com.example.newsapp.presentation.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.models.NewsResponse
import com.example.newsapp.data.repository.Repository
import com.example.newsapp.presentation.sealed.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository : Repository
) : ViewModel() {

    val searchNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = repository.searchAllNews(searchQuery)
        searchNews.postValue(handleSearchNews(response))
    }

    private fun handleSearchNews(response: Response<NewsResponse>): Resource<NewsResponse> {
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}