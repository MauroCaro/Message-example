package com.example.messagesexample.dataBase.dao

import androidx.room.*
import com.example.messagesexample.model.User
import io.reactivex.Completable
import io.reactivex.Single

@Dao
abstract class UserDao {

    @Query("SELECT * FROM ${User.USER_TABLE} WHERE id=:userId")
    abstract fun getUserById(userId: Int): Single<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUserList(userList: List<User>)

    @Query("DELETE FROM user_table")
    abstract fun deleteUsers(): Completable

    @Transaction
        open fun cleanAndInsertUsers(userList: List<User>) {
        deleteUsers()
        insertUserList(userList)
    }
}