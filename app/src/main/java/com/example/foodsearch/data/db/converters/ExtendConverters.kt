package com.example.foodsearch.data.db.converters

import androidx.room.TypeConverter
import com.example.foodsearch.domain.models.OtherModels.AnalyzedInstruction
import com.example.foodsearch.domain.models.OtherModels.Equipment
import com.example.foodsearch.domain.models.OtherModels.Ingredient
import com.example.foodsearch.domain.models.OtherModels.MetricMeasures
import com.example.foodsearch.domain.models.OtherModels.Step
import com.example.foodsearch.domain.models.OtherModels.UsMeasures
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ExtendConverters {
    @TypeConverter
    fun fromString(value: String?): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringToUsMeasures(value: String?): UsMeasures? {
        return Gson().fromJson(value, UsMeasures::class.java)
    }

    @TypeConverter
    fun fromUsMeasures(usMeasures: UsMeasures?): String? {
        return Gson().toJson(usMeasures)
    }

    @TypeConverter
    fun fromStringToMetricMeasures(value: String?): MetricMeasures? {
        return Gson().fromJson(value, MetricMeasures::class.java)
    }

    @TypeConverter
    fun fromMetricMeasures(metricMeasures: MetricMeasures?): String? {
        return Gson().toJson(metricMeasures)
    }

    @TypeConverter
    fun fromStringToAnalyzedInstruction(value: String?): AnalyzedInstruction? {
        return Gson().fromJson(value, AnalyzedInstruction::class.java)
    }


    @TypeConverter
    fun fromStringToStep(value: String?): Step? {
        return Gson().fromJson(value, Step::class.java)
    }

    @TypeConverter
    fun fromStep(step: Step?): String? {
        return Gson().toJson(step)
    }

    @TypeConverter
    fun fromStringToEquipment(value: String?): Equipment? {
        return Gson().fromJson(value, Equipment::class.java)
    }

    @TypeConverter
    fun fromEquipment(equipment: Equipment?): String? {
        return Gson().toJson(equipment)
    }


    @TypeConverter
    fun fromStringToAnalyzedInstructionList(value: String?): List<AnalyzedInstruction>? {
        val listType = object : TypeToken<List<AnalyzedInstruction>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromAnalyzedInstructionList(list: List<AnalyzedInstruction>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringToIngredientList(value: String?): List<Ingredient>? {
        val listType = object : TypeToken<List<Ingredient>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromIngredientList(list: List<Ingredient>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}


