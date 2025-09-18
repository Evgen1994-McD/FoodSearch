package com.example.foodsearch.di

import android.app.Application
import com.example.foodsearch.data.search.impl.SearchRepositoryImpl
import com.example.foodsearch.data.search.network.NetworkClient
import com.example.foodsearch.data.search.network.RetrofitNetworkClient
import com.example.foodsearch.data.search.network.SpoonacularApi
import com.example.foodsearch.domain.search.SearchInteractor
import com.example.foodsearch.domain.search.SearchRepository
import com.example.foodsearch.domain.search.impl.SearchInteractorImpl
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    private const val BASE_URL = "https://api.spoonacular.com/"

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Используем Gson конвертер
            .build()
    }

    @Provides
    @Singleton
    fun provideSpoonacularApi(retrofit: Retrofit): SpoonacularApi {
        return retrofit.create(SpoonacularApi::class.java)
    }

    // Создаем NetworkClient, принимающий готовую реализацию SpoonacularApi
    @Provides
    @Singleton
    fun provideNetworkClient(api: SpoonacularApi): NetworkClient {
        return RetrofitNetworkClient(api)
    }
}


    @Module
    @InstallIn(SingletonComponent::class)
    abstract class SearchModule {
        @Binds
        abstract fun bindSearchRepository(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository

        @Binds
        abstract fun bindSearchInteractor(searchInteractorImpl: SearchInteractorImpl): SearchInteractor
    }



