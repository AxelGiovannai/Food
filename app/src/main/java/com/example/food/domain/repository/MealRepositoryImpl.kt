package com.example.food.data.repository

import com.example.food.data.local.dao.MealDao
import com.example.food.data.remote.MealApi
import com.example.food.data.repository.mapper.toDomain
import com.example.food.data.repository.mapper.toEntity
import com.example.food.domain.model.Category
import com.example.food.domain.model.Meal
import com.example.food.domain.repository.MealRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MealRepositoryImpl(
    private val api: MealApi,
    private val dao: MealDao
) : MealRepository {

    override fun getCategoriesFlow(): Flow<List<Category>> {
        return dao.getCategories().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getMealsByCategoryFlow(category: String): Flow<List<Meal>> {
        return dao.getMealsByCategory(category).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun searchMealsFlow(query: String): Flow<List<Meal>> {
        return dao.searchMeals(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getMealByIdFlow(id: String): Flow<Meal?> {
        return dao.getMealById(id).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun refreshCategories() {
        val response = api.getCategories()
        val entities = response.categories.map { it.toEntity() }
        dao.insertCategories(entities)
    }

    override suspend fun refreshMealsByCategory(category: String) {
        val response = api.getMealsByCategory(category)
        val dtos = response.meals ?: emptyList()
        val entities = dtos.map { it.toEntity() }
        dao.insertMeals(entities)
    }

    override suspend fun searchMealsRemote(query: String) {
        val response = api.searchMeals(query)
        val dtos = response.meals ?: emptyList()
        val entities = dtos.map { it.toEntity() }
        dao.insertMeals(entities)
    }

    override suspend fun fetchMealDetails(id: String) {
        val response = api.getMealById(id)
        val dtos = response.meals ?: emptyList()
        val entities = dtos.map { it.toEntity() }
        dao.insertMeals(entities)
    }
}