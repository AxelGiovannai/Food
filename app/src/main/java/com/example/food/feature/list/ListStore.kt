package com.example.food.feature.list

import com.arkivanov.mvikotlin.core.store.Store
import com.example.food.domain.model.Category
import com.example.food.domain.model.Meal
import com.example.food.feature.list.ListStore.Intent
import com.example.food.feature.list.ListStore.Label
import com.example.food.feature.list.ListStore.State

interface ListStore : Store<Intent, State, Label> {


    sealed interface Intent {
        data class Search(val query: String) : Intent
        data class SelectCategory(val category: String) : Intent
        data class ClickMeal(val id: String) : Intent
        data object Retry : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val categories: List<Category> = emptyList(),
        val selectedCategory: String? = null,
        val meals: List<Meal> = emptyList(),
        val searchQuery: String = "",
        val error: String? = null
    )
    
    sealed interface Label {
        data class NavigateToDetail(val mealId: String) : Label
    }
}