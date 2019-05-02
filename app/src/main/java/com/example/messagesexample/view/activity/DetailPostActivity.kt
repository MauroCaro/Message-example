package com.example.messagesexample.view.activity

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.messagesexample.R
import com.example.messagesexample.model.Post
import com.example.messagesexample.model.User
import com.example.messagesexample.view.core.BaseActivity
import com.example.messagesexample.viewModel.DetailsPostViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detail_post.*
import java.util.ResourceBundle.getBundle

class DetailPostActivity : BaseActivity() {

    private lateinit var vm: DetailsPostViewModel

    private var makeFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_post)
        vm = ViewModelProviders.of(this, vmFactory).get(DetailsPostViewModel::class.java)
        var postInformation: Post? = null
        intent.extras?.let {
            postInformation = it.getParcelable(EXTRA_POST_INFORMATION)
        }
        postInformation?.let {
            vm.postInformation = it
        }
        initView()
        initModel()
        vm.getUser()
        vm.updateSeenPost()
    }


    private fun initModel() {
        vm.state.observe(this, Observer<DetailsPostViewModel.DetailPostState> {
            it?.let {
                update(it)
            }
        })
    }

    private fun update(state: DetailsPostViewModel.DetailPostState) {
        when (state.status) {
            DetailsPostViewModel.Status.ON_LOADING -> showLoadingDialog()
            DetailsPostViewModel.Status.ON_USER_LOADED -> userLoaded(state.userInformation)
            DetailsPostViewModel.Status.ON_UPDATED_FAVORITE -> favoriteUpdated(state.makeFavorite)
            DetailsPostViewModel.Status.ON_UPDATED_FAVORITE -> favoriteUpdated(state.makeFavorite)
        }
    }

    private fun favoriteUpdated(isfavorite: Boolean?) {
        isfavorite?.let {
            makeFavorite = it
            this.invalidateOptionsMenu()
        }
        dismissLoadingDialog()
    }

    private fun userLoaded(userInformation: User?) {
        userInformation?.let {
            detail_post_name_text?.text = it.name
            detail_post_email_text?.text = it.email
            detail_post_phone_text?.text = it.phone
            detail_post_web_site_text?.text = it.webSite
        }
        dismissLoadingDialog()
    }

    private fun initView() {
        vm.postInformation?.let {
            detail_post_description_text?.text = it.body
            makeFavorite = it.isFavorite
            this.invalidateOptionsMenu()
        }
        setSupportActionBar(main_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_options_detail, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (makeFavorite) {
            menu?.findItem(R.id.action_favorite)?.icon = ContextCompat.getDrawable(this, R.drawable.star)
        } else {
            menu?.findItem(R.id.action_favorite)?.icon = ContextCompat.getDrawable(this, R.drawable.star_empty)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_favorite -> vm.updateFavoritePost()
            else -> return false
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_POST_INFORMATION = "postInformation"
        fun start(context: Context, post: Post? = null) {
            val intent = Intent(context, DetailPostActivity::class.java)
            post.let {
                intent.putExtra(EXTRA_POST_INFORMATION, post)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                context.startActivity(intent)
                (context as? Activity)?.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            } ?: kotlin.run {
                Toast.makeText((context as? Activity), "There was an error loading the information", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}
