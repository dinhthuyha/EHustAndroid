package com.prdcv.ehust.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.prdcv.ehust.network.EHustClient
import com.prdcv.ehust.network.EHustService
import com.prdcv.ehust.utils.SharedPreferencesKey.EHUST
import com.prdcv.ehust.utils.SharedPreferencesKey.TOKEN
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRetrofit(
        sharedPreferences: SharedPreferences
    ): Retrofit {
        val token = sharedPreferences.getString(TOKEN,"")
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        }.build()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://104.215.150.77/api/" )
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").setLenient().create()
                )
            )
               if (!token.isNullOrEmpty())
                   retrofit.client(client)
            return retrofit.build()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(EHUST, Context.MODE_PRIVATE)
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