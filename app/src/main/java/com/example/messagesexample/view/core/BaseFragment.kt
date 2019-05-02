package com.example.messagesexample.view.core

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.example.messagesexample.dagger.injector.Injector

abstract class BaseFragment : Fragment(), BaseView {

    override fun showLoadingDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoadingDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @CallSuper
    open fun loadState(bundle: Bundle?) {

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.component().inject(this)
    }

}