package com.example.food.feature.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    onNavigateUp: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel.labels) {
        viewModel.labels.collectLatest { label ->
            when (label) {
                is DetailStore.Label.NavigateBack -> onNavigateUp()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.meal?.name ?: "Détails") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onIntent(DetailStore.Intent.ClickBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading && state.meal == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            else if (state.error != null && state.meal == null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = state.error ?: "Erreur", color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.onIntent(DetailStore.Intent.Retry) }) {
                        Text("Réessayer")
                    }
                }
            }
            else if (state.meal != null) {
                val meal = state.meal!!

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        AsyncImage(
                            model = meal.thumb,
                            contentDescription = meal.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        Text(
                            text = "Ingrédients",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    items(meal.ingredients) { ingredient ->
                        ListItem(
                            headlineContent = { Text(ingredient.name) },
                            trailingContent = { Text(ingredient.measure, fontWeight = FontWeight.SemiBold) }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Instructions",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                        Text(
                            text = meal.instructions,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}