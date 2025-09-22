package com.example.foodsearch.presentation.details.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.foodsearch.R
import com.example.foodsearch.domain.models.OtherModels
import com.example.foodsearch.domain.models.RecipeSummary
import com.example.foodsearch.presentation.search.adapter.OnRecipeClickListener

class IngredientsViewHolder(
    itemView: View,
    private val context: Context,
) :
    RecyclerView.ViewHolder(itemView) {

    private val name: TextView = itemView.findViewById(R.id.ingredient_name)
    private val value: TextView = itemView.findViewById(R.id.ingredient_value)
    private val unit: TextView = itemView.findViewById(R.id.ingredient_metric)
    private val image: ImageView = itemView.findViewById(R.id.ingredient_image)


    private val options = RequestOptions().centerCrop()
    private val radiusInDP = 2f
    private val densityMultiplier = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        1f,
        itemView.context.resources.displayMetrics
    )
    private val radiusInPX = radiusInDP * densityMultiplier


    @SuppressLint("CheckResult")
    fun bind(ingredient: OtherModels.Ingredient) {
        val extendIngredientLinks =
            "https://spoonacular.com/cdn/ingredients_100x100/" + ingredient.image


        name.text = ingredient.name
        value.text = ingredient.amount.toString()
        unit.text = ingredient.unit



        Glide.with(image.context)
            .load(extendIngredientLinks)
            .transform(RoundedCorners(radiusInPX.toInt()))
            .apply(options)
            .placeholder(R.drawable.ic_ph_kitchen)
            .error(R.drawable.ic_ph_kitchen)
            .into(image)


    }

}