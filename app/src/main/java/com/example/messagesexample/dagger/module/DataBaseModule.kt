package com.example.messagesexample.dagger.module

import android.content.Context
import androidx.room.Room
import com.example.messagesexample.dataBase.MessageDataBase
import com.example.messagesexample.dataBase.dao.PostDao
import com.example.messagesexample.dataBase.dao.UserDao
import com.example.messagesexample.util.Constants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataBaseModule(context: Context) {

    private val db: MessageDataBase =
        Room.databaseBuilder(context, MessageDataBase::class.java, Constants.DATA_BASE).fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideDatabase(): MessageDataBase = db

    @Provides
    @Singleton
    fun providePostDao(db: MessageDataBase): PostDao {
        return db.postDao()
    }

    @Provides
    @Singleton
    fun provideUserDao(db: MessageDataBase): UserDao {
        return db.userDao()
    }
}