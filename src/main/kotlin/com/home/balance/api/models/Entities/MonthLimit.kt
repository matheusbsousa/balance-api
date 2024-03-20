package com.home.balance.api.models.Entities

import com.home.balance.api.models.dtos.MonthLimitDto
import com.home.balance.api.utils.Constants
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "MONTH_LIMIT", schema = Constants.SCHEMA)
class MonthLimit(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var monthDescription: String,
    var month: Int,
    var year: Int,
    @OneToMany(mappedBy = "monthLimit")
    var limits: List<Limit>? = null
) {

    fun toDto(): MonthLimitDto {
        return MonthLimitDto(
            id = id,
            monthDescription = monthDescription,
            month = month,
            year = year,
            limits = limits?.map { it.toDto() }
        )
    }
}