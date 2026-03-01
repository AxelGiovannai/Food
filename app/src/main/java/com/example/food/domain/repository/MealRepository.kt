package com.example.food.domain.repository

import com.example.food.domain.model.Category
import com.example.food.domain.model.Meal
import kotlinx.coroutines.flow.Flow

interface MealRepository {

    fun getCategoriesFlow(): Flow<List<Category>>
    fun getMealsByCategoryFlow(category: String): Flow<List<Meal>>
    fun searchMealsFlow(query: String): Flow<List<Meal>>
    fun getMealByIdFlow(id: String): Flow<Meal?>

    suspend fun refreshCategories()
    suspend fun refreshMealsByCategory(category: String)
    suspend fun searchMealsRemote(query: String)
    suspend fun fetchMealDetails(id: String)
}