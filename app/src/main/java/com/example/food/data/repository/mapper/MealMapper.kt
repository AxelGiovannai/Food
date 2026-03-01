package com.example.food.data.repository.mapper

import com.example.food.data.local.entity.MealEntity
import com.example.food.data.remote.dto.MealDto
import com.example.food.domain.model.Ingredient
import com.example.food.domain.model.Meal

fun MealDto.toEntity(): MealEntity {
    return MealEntity(
        idMeal = idMeal, strMeal = strMeal, strMealThumb = strMealThumb,
        strCategory = strCategory, strInstructions = strInstructions,
        strIngredient1 = strIngredient1, strIngredient2 = strIngredient2, strIngredient3 = strIngredient3,
        strIngredient4 = strIngredient4, strIngredient5 = strIngredient5, strIngredient6 = strIngredient6,
        strIngredient7 = strIngredient7, strIngredient8 = strIngredient8, strIngredient9 = strIngredient9,
        strIngredient10 = strIngredient10, strIngredient11 = strIngredient11, strIngredient12 = strIngredient12,
        strIngredient13 = strIngredient13, strIngredient14 = strIngredient14, strIngredient15 = strIngredient15,
        strIngredient16 = strIngredient16, strIngredient17 = strIngredient17, strIngredient18 = strIngredient18,
        strIngredient19 = strIngredient19, strIngredient20 = strIngredient20,
        strMeasure1 = strMeasure1, strMeasure2 = strMeasure2, strMeasure3 = strMeasure3,
        strMeasure4 = strMeasure4, strMeasure5 = strMeasure5, strMeasure6 = strMeasure6,
        strMeasure7 = strMeasure7, strMeasure8 = strMeasure8, strMeasure9 = strMeasure9,
        strMeasure10 = strMeasure10, strMeasure11 = strMeasure11, strMeasure12 = strMeasure12,
        strMeasure13 = strMeasure13, strMeasure14 = strMeasure14, strMeasure15 = strMeasure15,
        strMeasure16 = strMeasure16, strMeasure17 = strMeasure17, strMeasure18 = strMeasure18,
        strMeasure19 = strMeasure19, strMeasure20 = strMeasure20
    )
}

fun MealEntity.toDomain(): Meal {

    val rawIngredients = listOf(
        strIngredient1 to strMeasure1, strIngredient2 to strMeasure2, strIngredient3 to strMeasure3,
        strIngredient4 to strMeasure4, strIngredient5 to strMeasure5, strIngredient6 to strMeasure6,
        strIngredient7 to strMeasure7, strIngredient8 to strMeasure8, strIngredient9 to strMeasure9,
        strIngredient10 to strMeasure10, strIngredient11 to strMeasure11, strIngredient12 to strMeasure12,
        strIngredient13 to strMeasure13, strIngredient14 to strMeasure14, strIngredient15 to strMeasure15,
        strIngredient16 to strMeasure16, strIngredient17 to strMeasure17, strIngredient18 to strMeasure18,
        strIngredient19 to strMeasure19, strIngredient20 to strMeasure20
    )

    val validIngredients = rawIngredients.mapNotNull { (ingredient, measure) ->
        if (!ingredient.isNullOrBlank()) {
            Ingredient(name = ingredient.trim(), measure = measure?.trim() ?: "")
        } else {
            null
        }
    }

    return Meal(
        id = idMeal,
        name = strMeal,
        thumb = strMealThumb,
        category = strCategory ?: "",
        instructions = strInstructions ?: "",
        ingredients = validIngredients
    )
}