package com.example.newsapp.di

import android.app.Application
import androidx.room.Room
import com.example.newsapp.data.local.database.ArticleDatabase
import com.example.newsapp.data.remote.api.Api
import com.example.newsapp.data.local.repository.DbRepository
import com.example.newsapp.data.remote.repository.Repository
import com.example.newsapp.domain.repositoryImpl.DbRepositoryImpl
import com.example.newsapp.domain.repositoryImpl.NetworkRepositoryImpl
import com.example.newsapp.presentation.activities.util.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideDatabase(app : Application) : ArticleDatabase {
        return Room.databaseBuilder(
            app,
            ArticleDatabase::class.java,
            "article.db"
        ).build()
    }


    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(api: Api) : Repository {
        return NetworkRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideDbRepository(db: ArticleDatabase): DbRepository {
        return DbRepositoryImpl(db)
    }
}