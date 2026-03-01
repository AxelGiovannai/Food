package com.example.food.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    factory: DetailStoreFactory
) : ViewModel() {

    private val mealId: String = checkNotNull(savedStateHandle["mealId"]) {
        "L'ID de la recette est manquant !"
    }

    private val store = factory.create(mealId)

    val state: StateFlow<DetailStore.State> = store.stateFlow(viewModelScope)
    val labels: Flow<DetailStore.Label> = store.labels

    fun onIntent(intent: DetailStore.Intent) {
        store.accept(intent)
    }

    override fun onCleared() {
        super.onCleared()
        store.dispose()
    }
}