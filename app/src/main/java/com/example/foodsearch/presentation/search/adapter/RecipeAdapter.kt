package com.example.foodsearch.presentation.search.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodsearch.R
import com.example.foodsearch.domain.models.RecipeSummary

class RecipeAdapter(
    private var recipeSummaries: List<RecipeSummary>?,
    private val listener: OnRecipeClickListener,  // тоже добавили листенер в конструктор класса
    private val context : Context
) : RecyclerView.Adapter<RecipeViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
        return RecipeViewHolder(view, listener, context)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            listener.onRecipeClicker(recipeSummaries!![position])
        }
        holder.bind(recipeSummaries!![position])
    }

    override fun getItemCount(): Int {
        return recipeSummaries!!.size
    }

}
