package com.example.messagesexample.dataBase.dao

import androidx.room.*
import com.example.messagesexample.model.Post
import io.reactivex.Completable
import io.reactivex.Single

@Dao
abstract class PostDao {

    @Query("SELECT * FROM post_table")
    abstract fun getAllPosts(): Single<List<Post>>

    @Query("SELECT * FROM post_table WHERE favorite = 1")
    abstract fun getAllFavoritesPosts(): Single<List<Post>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPostsList(postList: List<Post>)

    @Query("UPDATE post_table SET favorite= :isFavorite WHERE id = :postId")
    abstract fun updateFavorite(postId: Int, isFavorite: Boolean): Completable

    @Query("UPDATE post_table SET read= 1 WHERE id = :postId")
    abstract fun updateSeen(postId: Int): Completable

    @Query("DELETE FROM post_table WHERE id= :postId")
    abstract fun deletePostById(postId: Int): Completable

    @Query("DELETE FROM post_table")
    abstract fun deletePosts(): Completable

    @Transaction
    open fun cleanAndInsertPosts(postsList: List<Post>) {
        deletePosts()
        insertPostsList(postsList)
    }
}