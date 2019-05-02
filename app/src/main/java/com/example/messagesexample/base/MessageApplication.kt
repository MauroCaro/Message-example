package com.example.messagesexample.base

import android.app.Application
import android.content.Context
import com.example.messagesexample.dagger.injector.Injector
import com.example.messagesexample.dagger.component.DaggerMessageComponent
import com.example.messagesexample.dagger.component.MessageComponent
import com.example.messagesexample.dagger.module.ApiModule
import com.example.messagesexample.dagger.module.AppModule
import com.example.messagesexample.dagger.module.DataBaseModule

/**
 * Contains the instance of the application and generates the dagger build
 *
 * @author Mauricio Caro
 */
class MessageApplication : Application() {

    var applicationComponent: MessageComponent? = null

    override fun onCreate() {
        super.onCreate()
        Injector.init(buildComponent())
        Injector.component().inject(this)
        instance = this
        applicationComponent = buildComponent()
    }

    private fun buildComponent(): MessageComponent {
        return DaggerMessageComponent.builder()
            .appModule(AppModule(this))
            .apiModule(ApiModule(this))
            .dataBaseModule(DataBaseModule(this))
            .build()
    }

    companion object {
        var instance: MessageApplication? = null

        operator fun get(context: Context): MessageApplication {
            return context.applicationContext as MessageApplication
        }
    }
}