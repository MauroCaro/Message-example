package com.example.messagesexample.respository

import com.example.messagesexample.dataBase.dao.PostDao
import com.example.messagesexample.dataBase.dao.UserDao
import com.example.messagesexample.model.Post
import com.example.messagesexample.model.User
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Named

class MessageRepository(
    private val postDao: PostDao,
    private val userDao: UserDao,
    @Named("service") private val api: API
) {

    fun fetchPosts(): Single<List<Post>> {
        return api.getPosts()
    }

    fun fetchPostCache(): Single<List<Post>> {
        return postDao.getAllPosts()
    }

    fun savedPostOnDataBase(postsList: List<Post>) {
        return postDao.cleanAndInsertPosts(postsList)
    }

    fun deletePostFromDataBase(): Completable {
        return postDao.deletePosts()
    }

    fun deletePostByIdFromDataBase(idPost: Int): Completable {
        return postDao.deletePostById(idPost)
    }

    fun updateFavoritePost(postId: Int, isFavorite: Boolean): Completable {
        return postDao.updateFavorite(postId, isFavorite)
    }

    fun updateSeenPost(postId: Int): Completable {
        return postDao.updateSeen(postId)
    }

    fun fetchFavoritePosts(): Single<List<Post>> {
        return postDao.getAllFavoritesPosts()
    }

    fun fetchUsers(): Single<List<User>> {
        return api.getUsers()
    }

    fun fetchUserByIdCache(userId: Int): Single<User> {
        return userDao.getUserById(userId)
    }

    fun savedUsersOnDataBase(userList: List<User>) {
        return userDao.cleanAndInsertUsers(userList)
    }
}