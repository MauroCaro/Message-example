package com.example.messagesexample.view.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.messagesexample.R
import com.example.messagesexample.adapter.ItemPostAdapter
import com.example.messagesexample.adapter.ItemPostListener
import com.example.messagesexample.model.Post
import com.example.messagesexample.util.SwipeCallback
import com.example.messagesexample.view.activity.PostView
import com.example.messagesexample.view.core.BaseFragment
import kotlinx.android.synthetic.main.default_fragment_post.*

class DefaultPostFragment : BaseFragment(), ItemPostListener {


    private var listPost: ArrayList<Post>? = null
    lateinit var adapter: ItemPostAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.default_fragment_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadState(savedInstanceState ?: arguments)
        loadAdapter()
        initView()
    }

    private fun initView() {
        delete_all_posts?.setOnClickListener {
            val builder = AlertDialog.Builder(context!!)
            builder.setTitle(getString(R.string.dialog_delete_post_title))
            builder.setMessage(getString(R.string.dialog_delete_post_message))
            builder.setPositiveButton(getString(R.string.dialog_delete_post_confirmation)) { dialog, _ ->
                dialog.dismiss()
                listPost?.clear()
                adapter.notifyItemRangeChanged(0, listPost?.size!!)
                rv_default_post.visibility = View.INVISIBLE
                sad_image.visibility = View.VISIBLE
                onListenerPost.onDeleteAllItems()
            }
            builder.setNegativeButton(getString(R.string.dialog_delete_post_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }
    }

    private fun loadAdapter() {
        listPost?.let {
            adapter = ItemPostAdapter(context!!, this, it.toMutableList())
            rv_default_post?.adapter = adapter
            rv_default_post?.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context!!)
            rv_default_post?.isNestedScrollingEnabled = false
            val swipeHandler = object : SwipeCallback(context!!) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = rv_default_post?.adapter as ItemPostAdapter
                    adapter.removeAt(viewHolder.adapterPosition)
                    onListenerPost.deleteItemById(listPost?.get(viewHolder.adapterPosition)!!.id!!)
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(rv_default_post)
            rv_default_post?.addItemDecoration(
                androidx.recyclerview.widget.DividerItemDecoration(context!!, LinearLayout.VERTICAL)
            )
        }
    }

    override fun loadState(bundle: Bundle?) {
        super.loadState(bundle)
        bundle?.let {
            listPost = it.getParcelableArrayList(LIST_POSTS)
        }
    }

    override fun onFavoriteClicked(item: Post, position: Int) {
        item.isFavorite = !item.isFavorite
        listPost?.set(position, item)
        adapter.notifyItemChanged(position)
        onListenerPost.onFavoriteClicked(item)
    }

    companion object {
        private const val LIST_POSTS = "listPosts"
        lateinit var onListenerPost: PostView

        fun newInstance(postsList: ArrayList<Post>, onListenerView: PostView): DefaultPostFragment {
            val fragment = DefaultPostFragment()
            val args = Bundle()
            postsList?.let { args.putParcelableArrayList(LIST_POSTS, ArrayList(postsList)) }
            fragment.arguments = args
            onListenerPost = onListenerView
            return fragment
        }
    }
}