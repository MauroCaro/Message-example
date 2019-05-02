package com.example.messagesexample.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.messagesexample.model.Post.Companion.POST_TABLE
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = POST_TABLE)
data class Post(
    @PrimaryKey
    @ColumnInfo(name = ID)
    val id: Int?,
    @ColumnInfo(name = USER_ID)
    val userId: Int?,
    @ColumnInfo(name = TITLE)
    val title: String?,
    @ColumnInfo(name = BODY)
    val body: String?,
    @ColumnInfo(name = SEEN)
    var isAlreadySeen: Boolean = false,
    @ColumnInfo(name = FAVORITE)
    var isFavorite: Boolean = false

) : Parcelable {

    companion object {
        const val POST_TABLE = "post_table"
        const val ID = "id"
        const val USER_ID = "userId"
        const val TITLE = "title"
        const val BODY = "body"
        const val SEEN = "read"
        const val FAVORITE = "favorite"
    }
}

