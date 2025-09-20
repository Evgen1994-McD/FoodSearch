package com.example.foodsearch.presentation.search.adapter

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
import com.example.foodsearch.domain.models.RecipeSummary

class RecipeViewHolder(itemView: View, listener: OnRecipeClickListener, private val context:Context) :
    RecyclerView.ViewHolder(itemView) { // Добавили листенер в конструктор класса

    private val name: TextView = itemView.findViewById(R.id.name)
    private val summary: TextView = itemView.findViewById(R.id.tvsummary)
    private val servings: TextView = itemView.findViewById(R.id.tvServings)
    private val cookingTime: TextView = itemView.findViewById(R.id.tvCookingTime)
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
    fun bind(recipeSummary: RecipeSummary) {
        val tempSummary = recipeSummary.summary

        val index = tempSummary?.indexOf(".")
        val tempResult = tempSummary?.substring(0, (index?.plus(".".length) ?: "") as Int)
        val result = tempResult?.let { Regex("<[^>]*>").replace(it, "") }
/*
Выше выборка с кратким описанием из строки

 */
        name.text = recipeSummary.title
        summary.text = result
        servings.text= recipeSummary.servings.toString() +" "+  context.getString(R.string.servings)
        cookingTime.text=recipeSummary.readyInMinutes.toString() + " "+ context.getString(R.string.minutes)



        Glide.with(image.context)
            .load(recipeSummary.image)
            .transform(RoundedCorners(radiusInPX.toInt()))
            .apply(options)
//            .placeholder(R.drawable.ic_placeholder_45)
//            .error(R.drawable.ic_placeholder_45)
            .into(image)


    }

}
