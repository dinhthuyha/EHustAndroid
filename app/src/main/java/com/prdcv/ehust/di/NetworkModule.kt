package com.prdcv.ehust.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import com.prdcv.ehust.network.EHustClient
import com.prdcv.ehust.network.EHustService
import com.prdcv.ehust.utils.SharedPreferencesKey.EHUST
import com.prdcv.ehust.utils.SharedPreferencesKey.TOKEN
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.minio.MinioClient
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val localTimeDeserializer = JsonDeserializer { json, _, _ ->
        json?.let {
            LocalTime.parse(it.asString, DateTimeFormatter.ofPattern("HH:mm"))
        }
    }

    private val localDateDeserializer = JsonDeserializer { json, _, _ ->
        json?.let {
            LocalDate.parse(it.asString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }
    }

    private val localDateSerializer = JsonSerializer<LocalDate> { id, _, _ ->
        JsonPrimitive(id.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
    }

    @Provides
    @Singleton
    fun provideRetrofit(authenticator: Authenticator): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            })
            .authenticator(authenticator)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://104.215.150.77/api/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                        .setLenient()
                        .registerTypeAdapter(LocalTime::class.java, localTimeDeserializer)
                        .registerTypeAdapter(LocalDate::class.java, localDateDeserializer)
                        .registerTypeAdapter(LocalDate::class.java, localDateSerializer)
                        .create()
                )
            )
            .client(client)

        return retrofit.build()
    }

    @Provides
    @Singleton
    fun provideAuthenticator(sharedPreferences: SharedPreferences): Authenticator {
        return Authenticator { _, response ->
            if (response.request.header("Authorization") != null) {
                return@Authenticator null // Give up, we've already attempted to authenticate.
            }

            val token = sharedPreferences.getString(TOKEN, "")
            return@Authenticator response.request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        }
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
    fun provideMDLClient(mdlService: EHustService, minioClient: MinioClient): EHustClient {
        return EHustClient(mdlService, minioClient)
    }

    @Singleton
    @Provides
    fun minioClient(): MinioClient {
        return MinioClient.builder()
            .endpoint("http://104.215.150.77:9000")
            .credentials("iylnllsY1LIML1Kc", "NbjEzIcII2jjwCDju73D89urAdAGVrQx")
            .build()
    }
}