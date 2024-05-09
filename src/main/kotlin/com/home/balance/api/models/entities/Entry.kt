package com.home.balance.api.models.entities

import com.home.balance.api.models.dtos.EntryDto
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
    var originalDescription: String,
    var description: String? = null,
    var originalDate: Date,
    var date: Date? = null,
    var originalValue: Double,
    var value: Double? = null,
    var isIgnored: Boolean = false,
    var isCategorized: Boolean = false,

    @ManyToOne
    var category: Category? = null
) {
    fun toEntryDto() = EntryDto(
        id = id,
        date = date ?: originalDate,
        value = value ?: originalValue,
        description = description ?: originalDescription,
        category = category!!.toDto()
    )
}