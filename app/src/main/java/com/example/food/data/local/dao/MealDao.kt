package com.example.food.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.food.data.local.entity.CategoryEntity
import com.example.food.data.local.entity.MealEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {

    @Query("SELECT * FROM categories")
    fun getCategories(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Query("SELECT * FROM meals WHERE strCategory = :category")
    fun getMealsByCategory(category: String): Flow<List<MealEntity>>

    @Query("SELECT * FROM meals WHERE strMeal LIKE '%' || :searchQuery || '%'")
    fun searchMeals(searchQuery: String): Flow<List<MealEntity>>

    @Query("SELECT * FROM meals WHERE idMeal = :id")
    fun getMealById(id: String): Flow<MealEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<MealEntity>)
}