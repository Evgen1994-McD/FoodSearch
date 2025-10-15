package com.example.foodsearch.data.search.mapper

import com.example.foodsearch.data.search.dto.details.AnalyzedInstructionDto
import com.example.foodsearch.data.search.dto.details.EquipmentDto
import com.example.foodsearch.data.search.dto.details.IngredientDto
import com.example.foodsearch.data.search.dto.details.MeasuresDto
import com.example.foodsearch.data.search.dto.details.RecipeDetailsDto
import com.example.foodsearch.data.search.dto.details.StepDto
import com.example.foodsearch.data.search.dto.summary.RecipeSummaryDto
import com.example.foodsearch.domain.models.OtherModels
import com.example.foodsearch.domain.models.RecipeDetails
import com.example.foodsearch.domain.models.RecipeSummary
import javax.inject.Inject

/**
 * Маппер для преобразования DTO в Domain модели
 */
class RecipeDtoMapper @Inject constructor() {
    
    fun mapToDomain(dto: RecipeSummaryDto): RecipeSummary {
        return RecipeSummary(
            dto.id,
            dto.image,
            dto.title,
            dto.readyInMinutes,
            dto.servings,
            dto.summary
        )
    }
    
    fun mapToDomain(dto: RecipeDetailsDto): RecipeDetails {
        return RecipeDetails(
            id = dto.id,
            image = dto.image,
            imageType = dto.imageType,
            title = dto.title,
            readyInMinutes = dto.readyInMinutes,
            servings = dto.servings,
            sourceUrl = dto.sourceUrl,
            vegetarian = dto.vegetarian,
            vegan = dto.vegan,
            glutenFree = dto.glutenFree,
            dairyFree = dto.dairyFree,
            veryHealthy = dto.veryHealthy,
            cheap = dto.cheap,
            veryPopular = dto.veryPopular,
            sustainable = dto.sustainable,
            lowFodmap = dto.lowFodmap,
            weightWatcherSmartPoints = dto.weightWatcherSmartPoints,
            gaps = dto.gaps,
            preparationMinutes = dto.preparationMinutes,
            cookingMinutes = dto.cookingMinutes,
            aggregateLikes = dto.aggregateLikes,
            healthScore = dto.healthScore,
            creditsText = dto.creditsText,
            license = dto.license,
            sourceName = dto.sourceName,
            pricePerServing = dto.pricePerServing,
            extendedIngredients = dto.extendedIngredients?.map { mapIngredientToDomain(it) },
            summary = dto.summary,
            cuisines = dto.cuisines,
            dishTypes = dto.dishTypes,
            diets = dto.diets,
            occasions = dto.occasions,
            instructions = dto.instructions,
            analyzedInstructions = dto.analyzedInstructions?.map { mapAnalyzedInstructionToDomain(it) },
            spoonacularScore = dto.spoonacularScore,
            spoonacularSourceUrl = dto.spoonacularSourceUrl,
            isLike = false
        )
    }
    
    private fun mapIngredientToDomain(dto: IngredientDto): OtherModels.Ingredient {
        return OtherModels.Ingredient(
            id = dto.id ?: 0,
            aisle = dto.aisle ?: "",
            image = dto.image ?: "",
            consistency = dto.consistency ?: "",
            name = dto.name ?: "",
            nameClean = dto.nameClean ?: "",
            original = dto.original ?: "",
            originalName = dto.originalName ?: "",
            amount = dto.amount ?: 0.0,
            unit = dto.unit ?: "",
            meta = dto.meta ?: emptyList(),
            measures = mapMeasuresToDomain(dto.measures)
        )
    }
    
    private fun mapMeasuresToDomain(dto: MeasuresDto?): OtherModels.Measures {
        return OtherModels.Measures(
            us = OtherModels.UsMeasures(
                amount = dto?.us?.amount ?: 0.0,
                unitShort = dto?.us?.unitShort ?: "",
                unitLong = dto?.us?.unitLong ?: ""
            ),
            metric = OtherModels.MetricMeasures(
                amount = dto?.metric?.amount ?: 0.0,
                unitShort = dto?.metric?.unitShort ?: "",
                unitLong = dto?.metric?.unitLong ?: ""
            )
        )
    }
    
    private fun mapAnalyzedInstructionToDomain(dto: AnalyzedInstructionDto): OtherModels.AnalyzedInstruction {
        return OtherModels.AnalyzedInstruction(
            name = dto.name ?: "",
            steps = dto.steps?.map { mapStepToDomain(it) } ?: emptyList()
        )
    }
    
    private fun mapStepToDomain(dto: StepDto): OtherModels.Step {
        return OtherModels.Step(
            number = dto.number ?: 0,
            step = dto.step ?: "",
            ingredients = dto.ingredients?.map { mapIngredientToDomain(it) } ?: emptyList(),
            equipment = dto.equipment?.map { mapEquipmentToDomain(it) } ?: emptyList()
        )
    }
    
    private fun mapEquipmentToDomain(dto: EquipmentDto): OtherModels.Equipment {
        return OtherModels.Equipment(
            id = dto.id ?: 0,
            name = dto.name ?: "",
            localizedName = dto.localizedName ?: "",
            image = dto.image ?: ""
        )
    }
}
