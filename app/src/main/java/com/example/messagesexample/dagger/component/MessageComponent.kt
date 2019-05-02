package com.example.messagesexample.dagger.component

import com.example.messagesexample.adapter.ItemPostAdapter
import com.example.messagesexample.base.MessageApplication
import com.example.messagesexample.dagger.module.*
import com.example.messagesexample.view.core.BaseActivity
import com.example.messagesexample.view.core.BaseFragment
import com.example.messagesexample.viewModel.BaseViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ViewModelModule::class, RepositoryModule::class, DataBaseModule::class, AppModule::class,
        ApiModule::class]
)
interface MessageComponent {
    fun inject(messageApplication: MessageApplication)
    fun inject(baseActivity: BaseActivity)
    fun inject(baseViewModel: BaseViewModel)
    fun inject(adapter: ItemPostAdapter.PostItemViewHolder)
    fun inject(baseFragment: BaseFragment)

}