package com.example.android.politicalpreparedness.di


import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.MyApp
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.jsonadapter.DateAdapter
import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"

@Module
@InstallIn(SingletonComponent::class)
class RemoteDataModule {
/*
    @Provides
    @Singleton
    fun provideContext(): Application {
        return MyApp.instance
    }*/

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val url = original.url
                            .newBuilder()
                            .addQueryParameter("key", BuildConfig.API_KEY) // API_KEY value is inside local.properties file
                            .build()
                    val request = original
                            .newBuilder()
                            .url(url)
                            .build()
                    chain.proceed(request)
                }
                .addInterceptor(ChuckInterceptor(MyApp.instance))
                .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .build()
    }

    @Provides
    @Singleton
    fun provideRestInterface(retrofit: Retrofit): CivicsApiService {
        return retrofit.create(CivicsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
                .add(ElectionAdapter())
                .add(DateAdapter())
                .add(KotlinJsonAdapterFactory())
                .build()
    }
}