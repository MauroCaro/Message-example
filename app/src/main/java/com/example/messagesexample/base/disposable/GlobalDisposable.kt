package com.example.messagesexample.base.disposable

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

interface GlobalDisposable {
    fun addDisposable(key: Any, disposable: Disposable?): Boolean
    fun addDisposables(key: Any, vararg disposables: Disposable?)
    fun clearDisposable(key: Any)
    fun clearAllDisposables()
    fun getDisposable(key: Any): CompositeDisposable
}