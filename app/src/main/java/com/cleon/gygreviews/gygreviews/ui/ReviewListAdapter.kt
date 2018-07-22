package com.cleon.gygreviews.gygreviews.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cleon.gygreviews.gygreviews.R
import com.cleon.gygreviews.gygreviews.model.Review

class ReviewListAdapter(private val context: Context, private val data: MutableList<Review>?) : RecyclerView.Adapter<ReviewListAdapter.ReviewHolder>() {

    fun addItems(list: List<Review>) {
        data?.addAll(list)
        notifyDataSetChanged()
    }

    fun isDataNull() = data == null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewHolder {
        return ReviewHolder(layoutInflater.inflate(R.layout.review_cell, parent, false))
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: ReviewHolder, position: Int) {
        val review = data?.get(position)
        holder.bind(review)
    }

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)


    class ReviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val author: TextView = itemView.findViewById(R.id.autor)
        val message: TextView = itemView.findViewById(R.id.message)
        val rating: RatingBar = itemView.findViewById(R.id.ratingBar)
        fun bind(review: Review?) {
            author.text = review?.author?: ""
            title.text = review?.title?: ""
            message.text = review?.message?: ""
            rating.rating = review?.rating?.toFloat()?:0.0F
        }

    }
}
