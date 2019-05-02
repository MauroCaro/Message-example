package com.example.messagesexample.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.messagesexample.model.Post
import com.example.messagesexample.model.User
import com.example.messagesexample.respository.MessageRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailsPostViewModel @Inject constructor(val repository: MessageRepository) : BaseViewModel() {
    private val TAG = DetailsPostViewModel::class.java.simpleName

    val state: MutableLiveData<DetailsPostViewModel.DetailPostState> = MutableLiveData()
    var postInformation: Post? = null

    fun updateSeenPost() {
        postInformation?.let {
            addDisposable(
                repository.updateSeenPost(it.id!!).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { state.value = (DetailPostState.onLoading()) }
                    .subscribe(
                        {
                            Log.d(TAG, "Post already seen updated")
                        },
                        {
                            Log.d(TAG, "Error updating favorite from cache: ${it.localizedMessage}")
                        }
                    )
            )
        }
    }

    fun updateFavoritePost() {
        postInformation?.let {
            var makeFavorite = !it.isFavorite
            addDisposable(
                repository.updateFavoritePost(it.id!!, makeFavorite).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { state.value = (DetailPostState.onLoading()) }
                    .subscribe(
                        {
                            it?.let {
                                onFavoriteUpdated(makeFavorite)
                            }
                        },
                        {
                            Log.d(TAG, "Error updating favorite from cache: ${it.localizedMessage}")
                        }
                    )
            )
        }
    }

    private fun onFavoriteUpdated(makeFavorite: Boolean) {
        postInformation!!.isFavorite = makeFavorite
        state.value = DetailPostState.onFavoriteUpdated(makeFavorite)
    }

    fun getUser() {
        postInformation?.let {
            addDisposable(
                repository.fetchUserByIdCache(it.userId!!).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { state.value = (DetailPostState.onLoading()) }
                    .subscribe(
                        {
                            it?.let {
                                onUserLoaded(it)
                            } ?: kotlin.run {
                                fetchUsers()
                            }
                        },
                        {
                            Log.d(TAG, "Error getting user from cache: ${it.localizedMessage}")
                            fetchUsers()
                        }
                    )
            )
        }
    }

    private fun fetchUsers() {
        addDisposable(
            repository.fetchUsers().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { state.value = (DetailPostState.onLoading()) }
                .flatMap { users ->
                    Single.fromCallable {
                        onSavedUsers(users)
                    }
                        .subscribeOn(Schedulers.io())
                        .doOnError { Log.d(TAG, "Error saving users  on the DB: ${it.localizedMessage}") }
                        .map { users }
                }
                .subscribe(
                    { onUserListLoaded(it) },
                    { Log.d(TAG, "Error fetching posts: ${it.localizedMessage}") }
                )
        )
    }

    private fun onUserLoaded(it: User) {
        state.value = DetailPostState.onUserLoaded(it)
    }

    private fun onUserListLoaded(it: List<User>?) {
        getUser()
    }

    private fun onSavedUsers(it: List<User>?) {
        it?.let {
            repository.savedUsersOnDataBase(it)
        }
    }

    enum class Status {
        ON_LOADING, ON_USER_LOADED, ON_UPDATED_FAVORITE
    }

    data class DetailPostState(
        val status: Status,
        val userInformation: User? = null,
        val makeFavorite: Boolean? = null
    ) {

        companion object {

            fun onLoading(): DetailPostState {
                return DetailPostState(Status.ON_LOADING)
            }

            fun onUserLoaded(it: User): DetailPostState {
                return DetailPostState(Status.ON_USER_LOADED, userInformation = it)
            }

            fun onFavoriteUpdated(makeFavorite: Boolean): DetailPostState {
                return DetailPostState(Status.ON_UPDATED_FAVORITE, makeFavorite = makeFavorite)
            }
        }
    }
}