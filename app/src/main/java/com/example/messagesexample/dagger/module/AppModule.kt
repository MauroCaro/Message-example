package com.example.messagesexample.dagger.module

import android.content.Context
import com.example.messagesexample.base.MessageApplication
import com.example.messagesexample.base.disposable.Disposable
import com.example.messagesexample.base.disposable.GlobalDisposable
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: MessageApplication) {

    @Provides
    fun provideApplication(): MessageApplication = application

    @Provides
    fun provideContext(): Context = application

    @Singleton
    @Provides
    fun provideGlobalDisposable(): GlobalDisposable {
        return Disposable()
    }
}