package com.example.foodsearch.domain.models

class OtherModels {

    data class Ingredient(
        val id: Int,
        val aisle: String,
        val image: String,
        val consistency: String,
        val name: String,
        val nameClean: String,
        val original: String,
        val originalName: String,
        val amount: Double,
        val unit: String,
        val meta: List<String>,
        val measures: Measures
    )

    data class Measures(
        val us: UsMeasures,
        val metric: MetricMeasures
    )

    data class UsMeasures(
        val amount: Double,
        val unitShort: String,
        val unitLong: String
    )

    data class MetricMeasures(
        val amount: Double,
        val unitShort: String,
        val unitLong: String
    )

    data class AnalyzedInstruction(
        val name: String,
        val steps: List<Step>
    )

    data class Step(
        val number: Int,
        val step: String,
        val ingredients: List<Ingredient>,
        val equipment: List<Equipment>
    )

    data class Equipment(
        val id: Int,
        val name: String,
        val localizedName: String,
        val image: String
    )


}