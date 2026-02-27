package com.example.food.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.food.data.local.dao.MealDao
import com.example.food.data.local.entity.CategoryEntity
import com.example.food.data.local.entity.MealEntity

@Database(
    entities = [CategoryEntity::class, MealEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MealDatabase : RoomDatabase() {
    abstract val dao: MealDao

    companion object {
        const val DATABASE_NAME = "meal_db"
    }
}