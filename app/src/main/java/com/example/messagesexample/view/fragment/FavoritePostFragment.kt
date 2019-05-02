package com.example.messagesexample.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.messagesexample.R
import com.example.messagesexample.adapter.ItemPostAdapter
import com.example.messagesexample.adapter.ItemPostListener
import com.example.messagesexample.model.Post
import com.example.messagesexample.util.SwipeCallback
import com.example.messagesexample.view.activity.PostView
import com.example.messagesexample.view.core.BaseFragment
import kotlinx.android.synthetic.main.favorite_fragment_post.*

class FavoritePostFragment : BaseFragment(), ItemPostListener {

    private var listPost: ArrayList<Post>? = null
    lateinit var adapter: ItemPostAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.favorite_fragment_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadState(savedInstanceState ?: arguments)
        loadAdapter()
    }

    private fun loadAdapter() {
        listPost?.let {
            adapter = ItemPostAdapter(context!!, this, it.toMutableList())
            rv_favorite_post?.adapter = adapter
            rv_favorite_post?.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context!!)
            rv_favorite_post?.isNestedScrollingEnabled = false
            val swipeHandler = object : SwipeCallback(context!!) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = rv_favorite_post?.adapter as ItemPostAdapter
                    adapter.removeAt(viewHolder.adapterPosition)
                    onListenerPost.deleteItemById(listPost?.get(viewHolder.layoutPosition)!!.id!!)
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(rv_favorite_post)
            rv_favorite_post?.addItemDecoration(
                androidx.recyclerview.widget.DividerItemDecoration(context!!, LinearLayout.VERTICAL)
            )
        } ?: kotlin.run {
            sad_image.visibility = View.VISIBLE
        }
    }

    override fun loadState(bundle: Bundle?) {
        super.loadState(bundle)
        bundle?.let {
            listPost = it.getParcelableArrayList(FavoritePostFragment.LIST_POSTS)
        }
    }

    override fun onFavoriteClicked(item: Post, position: Int) {
        item.isFavorite = !item.isFavorite
        listPost?.remove(item)
        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeChanged(position, listPost?.size!!)
        adapter.notifyDataSetChanged()
        onListenerPost.onFavoriteClicked(item)
    }

    companion object {
        private const val LIST_POSTS = "listPosts"
        lateinit var onListenerPost: PostView

        fun newInstance(postsList: ArrayList<Post>?, onListenerView: PostView): FavoritePostFragment {
            val fragment = FavoritePostFragment()
            val args = Bundle()
            postsList?.let { args.putParcelableArrayList(LIST_POSTS, ArrayList(postsList)) }
            fragment.arguments = args
            onListenerPost = onListenerView
            return fragment
        }
    }
}