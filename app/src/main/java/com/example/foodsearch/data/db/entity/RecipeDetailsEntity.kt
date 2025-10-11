package com.example.foodsearch.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodsearch.domain.models.OtherModels.AnalyzedInstruction
import com.example.foodsearch.domain.models.OtherModels.Ingredient

@Entity(tableName = "recipe_details_table")
data class RecipeDetailsEntity(
    @PrimaryKey
    val id: Int, // Уникальный идентификатор рецепта
    val image: String?, // URL изображения рецепта
    val imageType: String?, // Тип изображения (например, "jpg")
    val title: String?, // Название рецепта
    val readyInMinutes: Int?, // Время приготовления в минутах
    val servings: Int?, // Количество порций
    val sourceUrl: String?, // URL источника рецепта
    val vegetarian: Boolean?, // Является ли рецепт вегетарианским
    val vegan: Boolean?, // Является ли рецепт веганским
    val glutenFree: Boolean?, // Является ли рецепт безглютеновым
    val dairyFree: Boolean?, // Является ли рецепт безмолочным
    val veryHealthy: Boolean?, // Является ли рецепт очень здоровым
    val cheap: Boolean?, // Является ли рецепт дешевым
    val veryPopular: Boolean?, // Является ли рецепт очень популярным
    val sustainable: Boolean?, // Является ли рецепт устойчивым
    val lowFodmap: Boolean?, // Подходит ли рецепт для диеты с низким содержанием FODMAP
    val weightWatcherSmartPoints: Int?, // Количество баллов по системе Weight Watchers
    val gaps: String?, // Информация о диете GAPS
    val preparationMinutes: Int?, // Время подготовки в минутах (может быть null)
    val cookingMinutes: Int?, // Время приготовления в минутах (может быть null)
    val aggregateLikes: Int?, // Количество лайков
    val healthScore: Double?, // Оценка здоровья
    val creditsText: String?, // Текст с указанием источника
    val license: String?, // Лицензия
    val sourceName: String?, // Имя источника
    val pricePerServing: Double?, // Цена за порцию
    val extendedIngredients: List<Ingredient>?, // Список ингредиентов
    val summary: String?, // Краткое описание рецепта
    val cuisines: List<String>?, // Список кухонь
    val dishTypes: List<String>?, // Типы блюд
    val diets: List<String>?, // Диеты
    val occasions: List<String>?, // Поводы
    val instructions: String?, // Инструкции по приготовлению
    val analyzedInstructions: List<AnalyzedInstruction>?, // Проанализированные инструкции
    val spoonacularScore: Double?, // Оценка Spoonacular
    val spoonacularSourceUrl: String?, // URL источника на Spoonacular
    val isLike: Boolean?
)