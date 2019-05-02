package com.example.messagesexample.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.messagesexample.R
import com.example.messagesexample.dagger.injector.Injector
import com.example.messagesexample.model.Post
import com.example.messagesexample.view.activity.DetailPostActivity

class ItemPostAdapter(
    val context: Context,
    val lister: ItemPostListener,
    var list: MutableList<Post>
) : androidx.recyclerview.widget.RecyclerView.Adapter<ItemPostAdapter.PostItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_list_item, parent, false)
        return PostItemViewHolder(view)
    }

    fun removeAt(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PostItemViewHolder, position: Int) {
        var item = list[position]
        val iconDrawable: Drawable = getIcon(item.isFavorite)
        holder.favorite.setImageDrawable(iconDrawable)
        if (item.isAlreadySeen || position > 20) {
            holder.imageRead.visibility = View.GONE
        } else if (position < 20) {
            holder.imageRead.visibility = View.VISIBLE
        }
        holder.title.text = item.title
        holder.body.text = item.body
        holder.favorite.setOnClickListener {
            lister.onFavoriteClicked(item, position)
        }
        holder.itemView.setOnClickListener {
            DetailPostActivity.start(context, item)
        }
    }

    private fun getIcon(isFavorite: Boolean): Drawable {
        return if (isFavorite) {
            context.getDrawable(R.drawable.star)
        } else {
            context.getDrawable(R.drawable.star_empty)
        }
    }


    class PostItemViewHolder internal constructor(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        internal var imageRead: ImageView = itemView.findViewById<View>(R.id.item_unread) as ImageView
        internal var title: TextView = itemView.findViewById<View>(R.id.item_title) as TextView
        internal var body: TextView = itemView.findViewById<View>(R.id.item_body) as TextView
        internal var favorite: ImageView = itemView.findViewById<View>(R.id.item_favorite) as ImageView

        init {
            Injector.component().inject(this)
        }

    }
}