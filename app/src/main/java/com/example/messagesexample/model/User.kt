package com.example.messagesexample.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.messagesexample.model.User.Companion.USER_TABLE
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = USER_TABLE)
data class User(
    @PrimaryKey
    @ColumnInfo(name = ID)
    val id: Int?,
    @ColumnInfo(name = NAME)
    val name: String?,
    @ColumnInfo(name = USER_NAME)
    val userName: String?,
    @ColumnInfo(name = EMAIL)
    val email: String?,
    @ColumnInfo(name = PHONE)
    var phone: String?,
    @ColumnInfo(name = WEB_SITE)
    var webSite: String?

) : Parcelable {

    companion object {
        const val USER_TABLE = "user_table"
        const val ID = "id"
        const val NAME = "name"
        const val USER_NAME = "username"
        const val EMAIL = "email"
        const val PHONE = "phone"
        const val WEB_SITE = "website"
    }
}