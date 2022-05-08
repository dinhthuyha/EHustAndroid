package com.prdcv.ehust.di

import com.google.gson.GsonBuilder
import com.prdcv.ehust.network.EHustClient
import com.prdcv.ehust.network.EHustService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://localhost:8080/api/" )
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").setLenient().create()
                )
            )
            .build()
    }


    @Provides
    fun provideMDLService(retrofit: Retrofit): EHustService {
        return retrofit.create(EHustService::class.java)
    }

    @Provides
    fun provideMDLClient(mdlService: EHustService): EHustClient {
        return EHustClient(mdlService)
    }
}