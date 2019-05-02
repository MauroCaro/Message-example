package com.example.messagesexample.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.messagesexample.BuildConfig
import com.example.messagesexample.dataBase.dao.PostDao
import com.example.messagesexample.dataBase.dao.UserDao
import com.example.messagesexample.model.Post
import com.example.messagesexample.model.User


@Database(entities = [Post::class, User::class], version = BuildConfig.DATABASE_VERSION, exportSchema = false)
abstract class MessageDataBase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun userDao(): UserDao
}