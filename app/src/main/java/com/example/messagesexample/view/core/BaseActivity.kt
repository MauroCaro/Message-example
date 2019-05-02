package com.example.messagesexample.view.core

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.messagesexample.R
import com.example.messagesexample.dagger.injector.Injector
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), BaseView {

    private var progress: ProgressBar? = null

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.component().inject(this)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        progress = findViewById(R.id.progress)
    }

    override fun showLoadingDialog() {
        progress?.visibility = View.VISIBLE
    }

    override fun dismissLoadingDialog() {
        progress?.visibility = View.GONE
    }
}