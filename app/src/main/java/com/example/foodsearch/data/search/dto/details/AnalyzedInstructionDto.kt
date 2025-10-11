package com.example.foodsearch.data.search.dto.details

data class AnalyzedInstructionDto(
    val name: String?,
    val steps: List<StepDto>?
)

data class StepDto(
    val number: Int?,
    val step: String?,
    val ingredients: List<IngredientDto>?,
    val equipment: List<EquipmentDto>?
)

data class EquipmentDto(
    val id: Int?,
    val name: String?,
    val localizedName: String?,
    val image: String?
)
