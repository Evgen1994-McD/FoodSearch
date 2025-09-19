package com.example.foodsearch.presentation.search.adapter

import android.annotation.SuppressLint
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.foodsearch.R
import com.example.foodsearch.domain.models.Recipe

class RecipeViewHolder(itemView: View, listener: OnRecipeClickListener) :
    RecyclerView.ViewHolder(itemView) { // Добавили листенер в конструктор класса

    private val name: TextView = itemView.findViewById(R.id.name)
    private val image: ImageView = itemView.findViewById(R.id.imMine)

    private val options = RequestOptions().centerCrop()
    private val radiusInDP = 2f
    private val densityMultiplier = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        1f,
        itemView.context.resources.displayMetrics
    )
    private val radiusInPX = radiusInDP * densityMultiplier





    @SuppressLint("CheckResult")
    fun bind(recipe: Recipe) {
        name.text = recipe.title



        Glide.with(image.context)
            .load(recipe.image)
            .transform(RoundedCorners(radiusInPX.toInt()))
            .apply(options)
//            .placeholder(R.drawable.ic_placeholder_45)
//            .error(R.drawable.ic_placeholder_45)
            .into(image)


    }

}
