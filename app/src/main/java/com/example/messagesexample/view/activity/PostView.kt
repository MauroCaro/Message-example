package com.example.messagesexample.view.activity

import com.example.messagesexample.model.Post

interface PostView {
    fun onFavoriteClicked(item: Post)

    fun onDeleteAllItems()

    fun deleteItemById(idPost:Int)
}