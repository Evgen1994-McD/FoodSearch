package com.example.foodsearch.presentation.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodsearch.R
import com.example.foodsearch.domain.models.Recipe

class RecipeAdapter(
    private var recipes: List<Recipe>?,
    private val listener: OnRecipeClickListener  // тоже добавили листенер в конструктор класса
) : RecyclerView.Adapter<RecipeViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
        return RecipeViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            listener.onRecipeClicker(recipes!![position])
        }
        holder.bind(recipes!![position])
    }

    override fun getItemCount(): Int {
        return recipes!!.size
    }

}
