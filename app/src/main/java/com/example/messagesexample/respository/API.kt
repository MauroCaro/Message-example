package com.example.messagesexample.respository

import com.example.messagesexample.model.Post
import com.example.messagesexample.model.User
import io.reactivex.Single
import retrofit2.http.GET

interface API {

    @GET("posts")
    abstract fun getPosts(): Single<List<Post>>

    @GET("users")
    abstract fun getUsers(): Single<List<User>>
}