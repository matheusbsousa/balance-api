package com.home.balance.api.models.entities

import com.home.balance.api.models.dtos.LimitDto
import com.home.balance.api.utils.Constants
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
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

    @OneToMany(mappedBy = "limit", cascade = [CascadeType.ALL])
    var limitCategories: List<LimitCategory>? = null
) {
    fun toDto(): LimitDto {
        return LimitDto(
            id = id,
            description = description,
            percentage = percentage,
            monthLimitId = monthLimit.id,
            limitCategories = limitCategories!!.map {it.toDto()}
            )
    }
}