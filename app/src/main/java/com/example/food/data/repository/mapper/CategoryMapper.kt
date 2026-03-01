package com.example.food.data.repository.mapper

import com.example.food.data.local.entity.CategoryEntity
import com.example.food.data.remote.dto.CategoryDto
import com.example.food.domain.model.Category

fun CategoryDto.toEntity(): CategoryEntity {
    return CategoryEntity(
        idCategory = idCategory,
        strCategory = strCategory,
        strCategoryThumb = strCategoryThumb,
        strCategoryDescription = strCategoryDescription
    )
}

fun CategoryEntity.toDomain(): Category {
    return Category(
        id = idCategory,
        name = strCategory,
        thumb = strCategoryThumb,
        description = strCategoryDescription
    )
}