package com.example.food.data.remote

import com.example.food.data.remote.dto.CategoryResponseDto
import com.example.food.data.remote.dto.MealResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    companion object {
        const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"
    }

    @GET("categories.php")
    suspend fun getCategories(): CategoryResponseDto

    @GET("search.php")
    suspend fun searchMeals(@Query("s") query: String): MealResponseDto

    @GET("filter.php")
    suspend fun getMealsByCategory(@Query("c") category: String): MealResponseDto

    @GET("lookup.php")
    suspend fun getMealById(@Query("i") id: String): MealResponseDto
}