package com.example.food.domain.model

data class Meal(
    val id: String,
    val name: String,
    val thumb: String,
    val category: String,
    val instructions: String,
    val ingredients: List<Ingredient>
)