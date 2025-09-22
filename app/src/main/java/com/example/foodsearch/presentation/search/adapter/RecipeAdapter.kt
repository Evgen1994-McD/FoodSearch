package com.example.foodsearch.presentation.search.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodsearch.R
import com.example.foodsearch.data.search.dto.summary.RecipeSummaryDto
import com.example.foodsearch.domain.models.RecipeSummary

class RecipeAdapter(
    private val listener: OnRecipeClickListener,
    private val context: Context,

) : PagingDataAdapter<RecipeSummary, RecipeViewHolder>(
    DIFF_CALLBACK
) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecipeSummary>() {
            override fun areItemsTheSame(oldItem: RecipeSummary, newItem: RecipeSummary): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RecipeSummary, newItem: RecipeSummary): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
        return RecipeViewHolder(view, listener, context)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {

        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                listener.onRecipeClicker(item)
            }
        }
    }
}