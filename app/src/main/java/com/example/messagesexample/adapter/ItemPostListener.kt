package com.example.messagesexample.adapter

import com.example.messagesexample.model.Post

interface ItemPostListener {
    fun onFavoriteClicked(item: Post, position: Int)
}