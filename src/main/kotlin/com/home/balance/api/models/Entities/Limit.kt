package com.home.balance.api.models.Entities

import com.home.balance.api.models.dtos.LimitDto
import com.home.balance.api.utils.Constants
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "LIMIT", schema = Constants.SCHEMA)
class Limit(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var description: String,
    var percentage: Double,

    @ManyToOne
    var monthLimit: MonthLimit,

    @ManyToMany
    @JoinTable(name = "CATEGORY_LIMIT",
        schema = Constants.SCHEMA,
        joinColumns = [JoinColumn(name = "LIMIT_ID")],
        inverseJoinColumns = [JoinColumn(name = "CATEGORY_ID")])
    var categories: List<Category>
) {
    fun toDto(): LimitDto {
        return LimitDto(
            id = id,
            description = description,
            percentage = percentage,
            monthLimitId = monthLimit.id,
            categories = categories.map { it.toDto() }
        )
    }
}