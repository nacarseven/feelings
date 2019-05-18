package com.nacarseven.feelings.feature

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nacarseven.feelings.R
import io.reactivex.subjects.PublishSubject
import io.reactivex.Observable
import kotlinx.android.synthetic.main.layout_tweet_item.view.*
import kotlin.properties.Delegates

class ResultAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val rowClick = PublishSubject.create<ItemClicked>()
    var list by Delegates.observable(emptyList<SearchViewModel.TweetState>()) { _, _, _ ->
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater
            .from(parent.context)
            .inflate(R.layout.layout_tweet_item, parent, false)
        )

    override fun getItemCount(): Int = list.size

    fun clickedTweet() : Observable<ItemClicked> = rowClick.hide()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val rowView = holder.itemView

        rowView.setOnClickListener { rowClick.onNext(ItemClicked(item.description, position)) }
        holder.bind(list[position])
    }

}

class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(tweet: SearchViewModel.TweetState) {
        view.txtTweetDescription.text = tweet.description
    }
}

data class ItemClicked(val description: String, val position: Int)

