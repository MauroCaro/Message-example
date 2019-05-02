package com.example.messagesexample.dagger.module

import android.content.Context
import com.example.messagesexample.respository.API
import com.example.messagesexample.util.Constants
import com.example.messagesexample.util.Constants.Companion.BASE_URL
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApiModule(val context: Context) {

    @Provides
    @Singleton
    @Named("service")
    fun provideClient(): API {
        val gson = GsonBuilder().setDateFormat(Constants.DATE_FORMAT).create()
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS).build()
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(API::class.java)
    }
}