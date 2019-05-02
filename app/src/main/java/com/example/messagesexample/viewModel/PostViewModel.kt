package com.example.messagesexample.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.messagesexample.model.Post
import com.example.messagesexample.respository.MessageRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PostViewModel @Inject constructor(val repository: MessageRepository) : BaseViewModel() {

    private val TAG = PostViewModel::class.java.simpleName

    val state: MutableLiveData<PostState> = MutableLiveData()

    var postsList: List<Post>? = null

    var postFavoritesList: List<Post>? = null


    enum class Status {
        ON_LOADING, ON_POSTS_LOADED, ON_FAVORITE_POSTS_LOADED, MESSAGE_EMPTY, DISMISS_LOADING
    }

    fun updateFavoritePost(item: Post) {
        item?.let {
            var makeFavorite = it.isFavorite
            addDisposable(
                repository.updateFavoritePost(it.id!!, makeFavorite).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { state.value = (PostState.onLoading()) }
                    .subscribe(
                        {
                            it?.let {
                                onFavoriteUpdated()
                            }
                        },
                        {
                            Log.d(TAG, "Error updating favorite from cache: ${it.localizedMessage}")
                        }
                    )
            )
        }
    }

    fun getFavoritePosts() {
        addDisposable(
            repository.fetchFavoritePosts().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { state.value = (PostState.onLoading()) }
                .subscribe(
                    {
                        if (it.isEmpty()) {
                            showMessageFavoritePost()
                        } else {
                            onFavoritesLoaded(it)
                        }
                    },
                    {
                        Log.d(TAG, "Error getting posts from cache: ${it.localizedMessage}")
                        fetchPosts()
                    }
                )
        )
    }

    fun getPosts() {
        addDisposable(
            repository.fetchPostCache().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { state.value = (PostState.onLoading()) }
                .subscribe(
                    {
                        if (it.isEmpty()) {
                            fetchPosts()
                        } else {
                            onPostsLoaded(it)
                        }
                    },
                    {
                        Log.d(TAG, "Error getting posts from cache: ${it.localizedMessage}")
                        fetchPosts()
                    }
                )
        )
    }

     fun fetchPosts() {
        addDisposable(
            repository.fetchPosts().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { state.value = (PostState.onLoading()) }
                .flatMap { posts ->
                    Single.fromCallable {
                        onSavedPosts(posts)
                    }
                        .subscribeOn(Schedulers.io())
                        .doOnError { Log.d(TAG, "Error saving posts on the DB: ${it.localizedMessage}") }
                        .map { posts }
                }
                .subscribe(
                    { onPostsLoaded(it) },
                    { Log.d(TAG, "Error fetching posts: ${it.localizedMessage}") }
                )
        )
    }

    fun deletePosts() {
        addDisposable(
            repository.deletePostFromDataBase().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { state.value = (PostState.onLoading()) }
                .subscribe(
                    { state.value = PostState.onDismissLoading() },
                    { Log.d(TAG, "Error deleting posts from cache: ${it.localizedMessage}") }
                )
        )
    }

    fun deletePostById(idPost :Int){
        idPost?.let {
            addDisposable(
                repository.deletePostByIdFromDataBase(idPost).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { state.value = (PostState.onLoading()) }
                    .subscribe(
                        {
                            it?.let {
                                onFavoriteUpdated()
                            }
                        },
                        {
                            Log.d(TAG, "Error updating favorite from cache: ${it.localizedMessage}")
                        }
                    )
            )
        }
    }


    private fun onFavoriteUpdated() {
        state.value = PostState.onDismissLoading()
    }


    private fun onSavedPosts(it: List<Post>?) {
        it?.let {
            repository.savedPostOnDataBase(it)
        }
    }

    private fun onPostsLoaded(it: List<Post>?) {
        postsList = it
        state.postValue(PostState.onPostLoaded())
    }

    private fun showMessageFavoritePost() {
        postFavoritesList = null
        state.value = PostState.onMessageEmptyFavorites()

    }

    private fun onFavoritesLoaded(it: List<Post>?) {
        postFavoritesList = it
        state.value = PostState.onFavoriteLoaded()
    }

    data class PostState(val status: Status) {

        companion object {

            fun onLoading(): PostState {
                return PostState(Status.ON_LOADING)
            }

            fun onDismissLoading(): PostState {
                return PostState(Status.DISMISS_LOADING)
            }

            fun onPostLoaded(): PostState {
                return PostState(Status.ON_POSTS_LOADED)
            }

            fun onFavoriteLoaded(): PostState {
                return PostState(Status.ON_FAVORITE_POSTS_LOADED)
            }

            fun onMessageEmptyFavorites(): PostState {
                return PostState(Status.MESSAGE_EMPTY)
            }
        }
    }
}