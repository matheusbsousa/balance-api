package com.home.balance.api.models.entities

import com.home.balance.api.models.dtos.EntryDto
import com.home.balance.api.models.enums.EntryType
import com.home.balance.api.utils.Constants
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "ENTRY", schema = Constants.SCHEMA)
class Entry(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var description: String,
    var date: Date,
    var value: Double,
    var originalDescription: String,
    var originalDate: Date,
    var originalValue: Double,
    var isIgnored: Boolean = false,
    var isCategorized: Boolean = false,
    var month: Int,
    var type: EntryType,

    @ManyToOne
    var category: Category
) {
    fun toEntryDto() = EntryDto(
        id = id,
        date = date,
        value = value,
        description = description,
        category = category.toDto(),
        type = type
    )
}