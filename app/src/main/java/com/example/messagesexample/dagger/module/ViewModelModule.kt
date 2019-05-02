package com.example.messagesexample.dagger.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.messagesexample.dagger.injector.InjectorViewModelFactory
import com.example.messagesexample.dagger.injector.ViewModelKey
import com.example.messagesexample.viewModel.DetailsPostViewModel
import com.example.messagesexample.viewModel.PostViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PostViewModel::class)
    abstract fun bindPostViewModel(viewModel: PostViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsPostViewModel::class)
    abstract fun bindDetailPostViewModel(viewModel: DetailsPostViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: InjectorViewModelFactory): ViewModelProvider.Factory
}