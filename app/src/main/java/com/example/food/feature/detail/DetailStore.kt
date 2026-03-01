package com.example.food.feature.detail

import com.arkivanov.mvikotlin.core.store.Store
import com.example.food.domain.model.Meal
import com.example.food.feature.detail.DetailStore.Intent
import com.example.food.feature.detail.DetailStore.Label
import com.example.food.feature.detail.DetailStore.State

interface DetailStore : Store<Intent, State, Label> {


    sealed interface Intent {
        data object ClickBack : Intent
        data object Retry : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val meal: Meal? = null,
        val error: String? = null
    )

    sealed interface Label {
        data object NavigateBack : Label
    }
}