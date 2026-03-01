package com.example.food.core.di

import android.content.Context
import androidx.room.Room
import com.example.food.data.local.MealDatabase
import com.example.food.data.local.dao.MealDao
import com.example.food.data.remote.MealApi
import com.example.food.data.repository.MealRepositoryImpl
import com.example.food.domain.repository.MealRepository
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideMealApi(): MealApi {
        val networkJson = Json { ignoreUnknownKeys = true }

        return Retrofit.Builder()
            .baseUrl(MealApi.BASE_URL)
            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(MealApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMealDatabase(@ApplicationContext context: Context): MealDatabase {
        return Room.databaseBuilder(
            context,
            MealDatabase::class.java,
            MealDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideMealDao(database: MealDatabase): MealDao {
        return database.dao
    }

    @Provides
    @Singleton
    fun provideMealRepository(
        api: MealApi,
        dao: MealDao
    ): MealRepository {
        return MealRepositoryImpl(api, dao)
    }

    @Provides
    @Singleton
    fun provideStoreFactory(): com.arkivanov.mvikotlin.core.store.StoreFactory {
        return com.arkivanov.mvikotlin.main.store.DefaultStoreFactory()
    }

}