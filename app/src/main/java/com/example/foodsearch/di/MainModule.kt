package com.example.foodsearch.di

import android.app.Application
import androidx.room.Room
import com.example.foodsearch.data.db.MainDb
import com.example.foodsearch.data.db.converters.ExtendConverters
import com.example.foodsearch.data.db.converters.RecipeDetailsDbConvertor
import com.example.foodsearch.data.db.converters.RecipeSummaryDbConvertor
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
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.jvm.java


@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    private const val BASE_URL = "https://api.spoonacular.com/"


    @Provides
    @Singleton
    fun provideMainDb(app: Application): MainDb {
        return Room.databaseBuilder(
            app,//Контекст
            MainDb::class.java, //Класс
            "recipe.db" //Имя
        ).build()
    }


    @Provides
    @Singleton
    fun provideRecipeSummaryDbConvertor(): RecipeSummaryDbConvertor {
        return RecipeSummaryDbConvertor()
    }

    @Provides
    @Singleton
    fun provideRecipeDetailsDbConvertor(): RecipeDetailsDbConvertor {
        return RecipeDetailsDbConvertor()
    }


    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()) // Используем Gson конвертер
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
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



