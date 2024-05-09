package com.home.balance.api.models.entities

import com.home.balance.api.models.dtos.LimitCategoryDto
import com.home.balance.api.utils.Constants
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "LIMIT_CATEGORY", schema = Constants.SCHEMA)
class LimitCategory(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne
    var limit: Limit,

    @ManyToOne
    var category: Category,

    var limitValue: Double? = 0.0,

    ) {
    fun toDto(): LimitCategoryDto {
        return LimitCategoryDto(
            id = id,
            description = category.name,
            categoryId = category.id!!,
            limit = limitValue ?: 0.0
        )
    }
}