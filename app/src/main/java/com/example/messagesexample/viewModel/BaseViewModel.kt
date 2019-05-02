package com.example.messagesexample.viewModel

import androidx.lifecycle.ViewModel
import com.example.messagesexample.dagger.injector.Injector
import com.example.messagesexample.base.disposable.GlobalDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject


abstract class BaseViewModel : ViewModel() {

    @Inject
    protected lateinit var globalDisposable: GlobalDisposable

    init {
        Injector.component()!!.inject(this)
    }

    fun addDisposable(key: Any, disposable: Disposable?) {
        disposable?.let { globalDisposable.addDisposable(key, it) }
    }

    fun addDisposable(disposable: Disposable?) {
        disposable?.let { globalDisposable.addDisposable(this, it) }
    }

    fun clearDisposable(key: Any) {
        globalDisposable.clearDisposable(key)
    }

    open fun clearDisposables() {
        globalDisposable.clearDisposable(this)
    }

    override fun onCleared() {
        super.onCleared()
        clearDisposables()
    }

}