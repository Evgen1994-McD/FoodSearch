package com.example.foodsearch.presentation.book.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodsearch.R
import com.example.foodsearch.domain.models.OtherModels
import com.example.foodsearch.domain.models.RecipeSummary
import com.example.foodsearch.presentation.search.adapter.OnRecipeClickListener
import com.example.foodsearch.presentation.search.adapter.RecipeViewHolder

class FavoriteAdapter(
    private var recipes:List<RecipeSummary>,
    private val context : Context,
    private val listener: OnRecipeClickListener,

) : RecyclerView.Adapter<FavoriteRecipeViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteRecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
        return FavoriteRecipeViewHolder(view, listener, context)
    }

    override fun onBindViewHolder(holder: FavoriteRecipeViewHolder, position: Int) {

        holder.bind(recipes!![position])

        holder.itemView.setOnClickListener {
            listener.onRecipeClicker(recipes!![position])
        }
    }

    override fun getItemCount(): Int {
        return recipes!!.size
    }

}
