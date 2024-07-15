package com.example.newsapp.presentation.home.vm

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_MOBILE
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.App
import com.example.newsapp.data.remote.repository.Repository
import com.example.newsapp.domain.model.NewsResponse
import com.example.newsapp.presentation.sealed.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    app : Application,
    private val repository : Repository
) : AndroidViewModel(app) {

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
        safeNewsCall(countryCode)
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

    private suspend fun safeNewsCall(countryCode: String) {
        try {
            if (hasInternetConnection()) {
                val response = repository.getAllNews(countryCode, pageNumber)
                tryResponse.postValue(handleBreakingNews(response))
            } else {
                tryResponse.postValue(Resource.Error("No internet connection"))
            }
        } catch (t : Throwable) {
            when(t) {
                is IOException -> tryResponse.postValue(Resource.Error("Network Failure"))
                else -> tryResponse.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun hasInternetConnection() : Boolean {
        val connectivityService = getApplication<App>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityService.activeNetwork ?: return false
            val capability = connectivityService.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capability.hasTransport(TRANSPORT_WIFI) -> true
                capability.hasTransport(TRANSPORT_CELLULAR) -> true
                capability.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityService.activeNetworkInfo?.run {
                return when(type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}