package com.example.messagesexample.dagger.module

import com.example.messagesexample.dataBase.dao.PostDao
import com.example.messagesexample.dataBase.dao.UserDao
import com.example.messagesexample.respository.API
import com.example.messagesexample.respository.MessageRepository
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton


@Module
class RepositoryModule {
    @Provides
    @Singleton
    internal fun provideCommunityRepository(
        @Named("service") api: API,
        postDao: PostDao, userDao: UserDao
    ): MessageRepository {
        return MessageRepository(postDao, userDao, api)
    }
}