package com.example.foodsearch.data.db.converters

import com.example.foodsearch.data.db.entity.RecipeSummaryEntity
import com.example.foodsearch.domain.models.RecipeSummary

class RecipeSummaryDbConvertor {
    fun map(recipe: RecipeSummary): RecipeSummaryEntity {
        return RecipeSummaryEntity(
            id = recipe.id,
            image = recipe.image,
            title = recipe.title,
            readyInMinutes = recipe.readyInMinutes,
            servings = recipe.servings,
            summary = recipe.summary,
            isLike = recipe.isLike

        )

    }

    fun map(recipe: RecipeSummaryEntity): RecipeSummary {
        return RecipeSummary(
            id = recipe.id,
            image = recipe.image,
            title = recipe.title,
            readyInMinutes = recipe.readyInMinutes,
            servings = recipe.servings,
            summary = recipe.summary,
            isLike = recipe.isLike
        )

    }

}