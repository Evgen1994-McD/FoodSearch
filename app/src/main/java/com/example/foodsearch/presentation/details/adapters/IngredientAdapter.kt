package com.example.foodsearch.presentation.details.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodsearch.R
import com.example.foodsearch.domain.models.OtherModels
import com.example.foodsearch.domain.models.RecipeSummary
import com.example.foodsearch.presentation.search.adapter.OnRecipeClickListener
import com.example.foodsearch.presentation.search.adapter.RecipeViewHolder

class IngredientAdapter(
    private var ingredients: List<OtherModels.Ingredient>?,
    private val context: Context,

    ) : RecyclerView.Adapter<IngredientsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.ingredient_item, parent, false)
        return IngredientsViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {

        holder.bind(ingredients!![position])
    }

    override fun getItemCount(): Int {
        return ingredients!!.size
    }

}
