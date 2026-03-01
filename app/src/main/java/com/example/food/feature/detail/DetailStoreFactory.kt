package com.example.food.feature.detail

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.food.domain.model.Meal
import com.example.food.domain.repository.MealRepository
import com.example.food.feature.detail.DetailStore.Intent
import com.example.food.feature.detail.DetailStore.Label
import com.example.food.feature.detail.DetailStore.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val repository: MealRepository
) {

    fun create(mealId: String): DetailStore =
        object : DetailStore, Store<Intent, State, Label> by storeFactory.create(
            name = "DetailStore_$mealId",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Action.Init),
            executorFactory = { ExecutorImpl(mealId) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data object Init : Action
    }

    private sealed interface Message {
        data class Loading(val isLoading: Boolean) : Message
        data class MealLoaded(val meal: Meal?) : Message
        data class Error(val message: String?) : Message
    }

    private inner class ExecutorImpl(
        private val mealId: String
    ) : CoroutineExecutor<Intent, Action, State, Message, Label>() {

        override fun executeAction(action: Action) {
            when (action) {
                is Action.Init -> {
                    repository.getMealByIdFlow(mealId)
                        .onEach { dispatch(Message.MealLoaded(it)) }
                        .launchIn(scope)
                    fetchMealDetails()
                }
            }
        }

        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.ClickBack -> publish(Label.NavigateBack)
                is Intent.Retry -> fetchMealDetails()
            }
        }

        private fun fetchMealDetails() {
            scope.launch {
                try {
                    dispatch(Message.Loading(true))
                    repository.fetchMealDetails(mealId)
                    dispatch(Message.Error(null))
                } catch (e: Exception) {
                    dispatch(Message.Error("Impossible de charger les détails. Vérifiez votre connexion."))
                } finally {
                    dispatch(Message.Loading(false))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Message> {
        override fun State.reduce(msg: Message): State =
            when (msg) {
                is Message.Loading -> copy(isLoading = msg.isLoading)
                is Message.MealLoaded -> copy(meal = msg.meal)
                is Message.Error -> copy(error = msg.message)
            }
    }
}