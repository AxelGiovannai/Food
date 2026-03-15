package com.example.food.core.di

import android.content.Context
import androidx.room.Room
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
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
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.example.food.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideMealApi(okHttpClient: OkHttpClient): MealApi {
        val networkJson = Json { ignoreUnknownKeys = true }

        return Retrofit.Builder()
            .baseUrl(MealApi.BASE_URL)
            .client(okHttpClient)
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
    fun provideStoreFactory(): StoreFactory {
        return if (BuildConfig.DEBUG) {
            LoggingStoreFactory(DefaultStoreFactory())
        } else {
            DefaultStoreFactory()
        }
    }

}