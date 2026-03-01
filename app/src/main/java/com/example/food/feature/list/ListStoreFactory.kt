package com.example.food.feature.list

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.food.domain.model.Category
import com.example.food.domain.model.Meal
import com.example.food.domain.repository.MealRepository
import com.example.food.feature.list.ListStore.Intent
import com.example.food.feature.list.ListStore.Label
import com.example.food.feature.list.ListStore.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val repository: MealRepository
) {

    fun create(): ListStore =
        object : ListStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ListStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Action.Init),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}
    private sealed interface Action {
        data object Init : Action
    }
    private sealed interface Message {
        data class Loading(val isLoading: Boolean) : Message
        data class CategoriesLoaded(val categories: List<Category>) : Message
        data class MealsLoaded(val meals: List<Meal>) : Message
        data class SearchQueryChanged(val query: String) : Message
        data class CategorySelected(val category: String?) : Message
        data class Error(val message: String?) : Message
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Message, Label>() {

        private var mealsFlowJob: Job? = null

        override fun executeAction(action: Action) {
            when (action) {
                is Action.Init -> {
                    repository.getCategoriesFlow()
                        .onEach { dispatch(Message.CategoriesLoaded(it)) }
                        .launchIn(scope)
                    refreshData()
                }
            }
        }

        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.Retry -> refreshData()

                is Intent.SelectCategory -> {
                    dispatch(Message.CategorySelected(intent.category))
                    dispatch(Message.SearchQueryChanged(""))
                    observeMealsByCategory(intent.category)

                    scope.launch {
                        try {
                            dispatch(Message.Loading(true))
                            repository.refreshMealsByCategory(intent.category)
                            dispatch(Message.Error(null))
                        } catch (e: Exception) {
                            dispatch(Message.Error("Erreur de connexion. Vérifiez votre réseau."))
                        } finally {
                            dispatch(Message.Loading(false))
                        }
                    }
                }

                is Intent.Search -> {
                    dispatch(Message.SearchQueryChanged(intent.query))
                    dispatch(Message.CategorySelected(null))
                    observeMealsBySearch(intent.query)

                    if (intent.query.isNotBlank()) {
                        scope.launch {
                            try {
                                dispatch(Message.Loading(true))
                                repository.searchMealsRemote(intent.query)
                                dispatch(Message.Error(null))
                            } catch (e: Exception) {
                                dispatch(Message.Error("Erreur de recherche. Vérifiez votre réseau."))
                            } finally {
                                dispatch(Message.Loading(false))
                            }
                        }
                    } else {
                        refreshData()
                    }
                }

                is Intent.ClickMeal -> publish(Label.NavigateToDetail(intent.id))
            }
        }

        private fun refreshData() {
            scope.launch {
                try {
                    dispatch(Message.Loading(true))
                    repository.refreshCategories()
                    repository.refreshMealsByCategory("Beef")
                    observeMealsByCategory("Beef")
                    dispatch(Message.CategorySelected("Beef"))
                    dispatch(Message.Error(null))
                } catch (e: Exception) {
                    dispatch(Message.Error("Impossible de mettre à jour les données. Vérifiez votre connexion réseau."))
                } finally {
                    dispatch(Message.Loading(false))
                }
            }
        }

        private fun observeMealsByCategory(category: String) {
            mealsFlowJob?.cancel()
            mealsFlowJob = repository.getMealsByCategoryFlow(category)
                .onEach { dispatch(Message.MealsLoaded(it)) }
                .launchIn(scope)
        }

        private fun observeMealsBySearch(query: String) {
            mealsFlowJob?.cancel()
            mealsFlowJob = repository.searchMealsFlow(query)
                .onEach { dispatch(Message.MealsLoaded(it)) }
                .launchIn(scope)
        }
    }

    private object ReducerImpl : Reducer<State, Message> {
        override fun State.reduce(msg: Message): State =
            when (msg) {
                is Message.Loading -> copy(isLoading = msg.isLoading)
                is Message.CategoriesLoaded -> copy(categories = msg.categories)
                is Message.MealsLoaded -> copy(meals = msg.meals)
                is Message.SearchQueryChanged -> copy(searchQuery = msg.query)
                is Message.CategorySelected -> copy(selectedCategory = msg.category)
                is Message.Error -> copy(error = msg.message)
            }
    }
}