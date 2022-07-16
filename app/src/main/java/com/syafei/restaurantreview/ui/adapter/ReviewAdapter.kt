package com.syafei.restaurantreview.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.syafei.restaurantreview.R

class ReviewAdapter(private val listReview: List<String>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvRv = view.findViewById<TextView>(R.id.tvItem)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.tvRv.text = listReview[position]
    }

    override fun getItemCount(): Int = listReview.size

}