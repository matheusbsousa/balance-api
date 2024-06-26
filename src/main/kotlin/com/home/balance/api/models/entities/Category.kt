package com.home.balance.api.models.entities

import com.home.balance.api.models.dtos.CategoryDto
import com.home.balance.api.utils.Constants.SCHEMA
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "CATEGORY", schema = SCHEMA)
class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String,
    var values: String,
    var colorHex: String? = null,
){
    fun toDto(): CategoryDto {
        return CategoryDto(
            id = id,
            name = name,
            values = values.split(","),
            colorHex = colorHex
        )
    }
}