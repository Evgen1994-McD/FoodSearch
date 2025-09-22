package com.example.foodsearch.data.db.converters

import com.example.foodsearch.data.db.entity.RecipeDetailsEntity
import com.example.foodsearch.domain.models.RecipeDetails

class RecipeDetailsDbConvertor {

    fun map(recipe: RecipeDetails): RecipeDetailsEntity {
        return RecipeDetailsEntity(
            id = recipe.id,
            image = recipe.image,
            imageType = recipe.imageType,
            title = recipe.title,
            readyInMinutes = recipe.readyInMinutes,
            servings = recipe.servings,
            sourceUrl = recipe.sourceUrl,
            vegetarian = recipe.vegetarian,
            vegan = recipe.vegan,
            glutenFree = recipe.glutenFree,
            dairyFree = recipe.dairyFree,
            veryHealthy = recipe.veryHealthy,
            cheap = recipe.cheap,
            veryPopular = recipe.veryPopular,
            sustainable = recipe.sustainable,
            lowFodmap = recipe.lowFodmap,
            weightWatcherSmartPoints = recipe.weightWatcherSmartPoints,
            gaps = recipe.gaps,
            preparationMinutes = recipe.preparationMinutes,
            cookingMinutes = recipe.cookingMinutes,
            aggregateLikes = recipe.aggregateLikes,
            healthScore = recipe.healthScore,
            creditsText = recipe.creditsText,
            license = recipe.license,
            sourceName = recipe.sourceName,
            pricePerServing = recipe.pricePerServing,
            extendedIngredients = recipe.extendedIngredients,
            summary = recipe.summary,
            cuisines = recipe.cuisines,
            dishTypes = recipe.dishTypes,
            diets = recipe.diets,
            occasions = recipe.occasions,
            instructions = recipe.instructions,
            analyzedInstructions = recipe.analyzedInstructions,
            spoonacularScore = recipe.spoonacularScore,
            spoonacularSourceUrl = recipe.spoonacularSourceUrl,
            recipe.isLike
        )
    }

    fun map(recipe: RecipeDetailsEntity): RecipeDetails? {
        return RecipeDetails(
            id = recipe.id,
            image = recipe.image,
            imageType = recipe.imageType,
            title = recipe.title,
            readyInMinutes = recipe.readyInMinutes,
            servings = recipe.servings,
            sourceUrl = recipe.sourceUrl,
            vegetarian = recipe.vegetarian,
            vegan = recipe.vegan,
            glutenFree = recipe.glutenFree,
            dairyFree = recipe.dairyFree,
            veryHealthy = recipe.veryHealthy,
            cheap = recipe.cheap,
            veryPopular = recipe.veryPopular,
            sustainable = recipe.sustainable,
            lowFodmap = recipe.lowFodmap,
            weightWatcherSmartPoints = recipe.weightWatcherSmartPoints,
            gaps = recipe.gaps,
            preparationMinutes = recipe.preparationMinutes,
            cookingMinutes = recipe.cookingMinutes,
            aggregateLikes = recipe.aggregateLikes,
            healthScore = recipe.healthScore,
            creditsText = recipe.creditsText,
            license = recipe.license,
            sourceName = recipe.sourceName,
            pricePerServing = recipe.pricePerServing,
            extendedIngredients = recipe.extendedIngredients,
            summary = recipe.summary,
            cuisines = recipe.cuisines,
            dishTypes = recipe.dishTypes,
            diets = recipe.diets,
            occasions = recipe.occasions,
            instructions = recipe.instructions,
            analyzedInstructions = recipe.analyzedInstructions,
            spoonacularScore = recipe.spoonacularScore,
            spoonacularSourceUrl = recipe.spoonacularSourceUrl,
            recipe.isLike
        )
    }
}