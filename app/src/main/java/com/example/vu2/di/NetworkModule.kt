package com.example.vu2.di


import com.example.vu2.data.api.ApiService
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module  //this class defines what dependencies will be
@InstallIn(SingletonComponent::class)


object NetworkModule {
    @Provides
    @Singleton
    //Creates a Retrofit Instance that knows where serve is n how to handle JSON
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://nit3213api.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    }

    @Provides
    @Singleton
    //Defines what API call exists(login,dashboard)
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }



}

