package com.example.messagesexample.view.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.messagesexample.R
import com.example.messagesexample.model.Post
import com.example.messagesexample.view.activity.fragment.DefaultPostFragment
import com.example.messagesexample.view.core.BaseActivity
import com.example.messagesexample.view.fragment.FavoritePostFragment
import com.example.messagesexample.viewModel.PostViewModel
import kotlinx.android.synthetic.main.activity_home.*

class PostActivity : BaseActivity(), PostView {


    private lateinit var vm: PostViewModel
    private lateinit var fragmentSelected: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        vm = ViewModelProviders.of(this, vmFactory).get(PostViewModel::class.java)
        initViews()
        initListenersBottomNavigation()
        initModel()
    }

    private fun initModel() {
        vm.state.observe(this, Observer<PostViewModel.PostState> {
            it?.let {
                update(it)
            }
        })
    }

    private fun update(state: PostViewModel.PostState) {
        when (state.status) {
            PostViewModel.Status.ON_LOADING -> showLoadingDialog()
            PostViewModel.Status.ON_POSTS_LOADED -> postsLoaded()
            PostViewModel.Status.ON_FAVORITE_POSTS_LOADED -> favoritesLoaded()
            PostViewModel.Status.MESSAGE_EMPTY -> showMessageEmpty()
            PostViewModel.Status.DISMISS_LOADING -> dismissLoadingDialog()
        }
    }

    private fun showMessageEmpty() {
        favoritesLoaded()
        Toast.makeText(this, "There is not favorites added", Toast.LENGTH_LONG).show()
    }

    private fun favoritesLoaded() {
        dismissLoadingDialog()
        fragmentSelected = if (vm.postFavoritesList != null) {
            FavoritePostFragment.newInstance(vm.postFavoritesList as ArrayList<Post>, this)
        } else {
            FavoritePostFragment.newInstance(null, this)
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.home_fragment, fragmentSelected, "favorite")
        transaction.commit()
    }

    private fun postsLoaded() {
        dismissLoadingDialog()
        fragmentSelected = DefaultPostFragment.newInstance(vm.postsList as ArrayList<Post>, this)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.home_fragment, fragmentSelected, "default")
        transaction.commit()
    }

    private fun initViews() {
        main_toolbar.title = getString(R.string.title_posts)
        setSupportActionBar(main_toolbar)
        bottom_navigation.selectedItemId = R.id.action_home
    }

    private fun initListenersBottomNavigation() {
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home ->
                    vm.getPosts()
                R.id.action_favorites ->
                    vm.getFavoritePosts()
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        if (::fragmentSelected.isInitialized) {
            if (fragmentSelected.fragmentManager!!.findFragmentByTag("default") != null) {
                vm.getPosts()
            } else {
                vm.getFavoritePosts()
            }
        } else {
            vm.getPosts()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_refresh -> vm.fetchPosts()
            else -> return false
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onFavoriteClicked(item: Post) {
        vm.updateFavoritePost(item)
    }

    override fun onDeleteAllItems() {
        vm.deletePosts()
    }

    override fun deleteItemById(idPost: Int) {
        vm.deletePostById(idPost)
    }
}
