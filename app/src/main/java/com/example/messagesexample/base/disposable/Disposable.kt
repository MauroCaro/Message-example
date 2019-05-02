package com.example.messagesexample.base.disposable

import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class Disposable : GlobalDisposable {
    @Synchronized
    override fun addDisposable(key: Any, disposable: Disposable?): Boolean {
        return disposable?.let {
            val added = getDisposable(key).add(disposable)
            if (!added) {
                Log.i("already disposed", disposable.toString())
            }

            added
        } ?: false
    }

    @Synchronized
    override fun addDisposables(key: Any, vararg disposables: Disposable?) {
        disposables.forEach {
            addDisposable(key, it)
        }
    }

    @Synchronized
    override fun clearDisposable(key: Any) {
        getDisposable(key).clear()
    }

    @Synchronized
    override fun clearAllDisposables() {
        sGlobalDisposable.keys.forEach { key ->
            clearDisposable(key)
        }
    }

    @Synchronized
    override fun getDisposable(key: Any): CompositeDisposable {
        if (sGlobalDisposable[key] == null) {
            sGlobalDisposable[key] = CompositeDisposable()
        }

        return sGlobalDisposable[key]!!
    }

    companion object {
        private val sGlobalDisposable = HashMap<Any, CompositeDisposable>()
    }

}