package com.example.foodsearch.data.search.dto.details

data class IngredientDto(
    val id: Int?,
    val aisle: String?,
    val image: String?,
    val consistency: String?,
    val name: String?,
    val nameClean: String?,
    val original: String?,
    val originalName: String?,
    val amount: Double?,
    val unit: String?,
    val meta: List<String>?,
    val measures: MeasuresDto?
)

data class MeasuresDto(
    val us: UsMeasuresDto?,
    val metric: MetricMeasuresDto?
)

data class UsMeasuresDto(
    val amount: Double?,
    val unitShort: String?,
    val unitLong: String?
)

data class MetricMeasuresDto(
    val amount: Double?,
    val unitShort: String?,
    val unitLong: String?
)
