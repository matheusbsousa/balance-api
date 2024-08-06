package com.home.balance.api.models.dtos

import com.home.balance.api.models.enums.EntryType
import java.util.*

class EntryDto(
    var id: Long? = null,
    var date: Date,
    var value: Double,
    var category: CategoryDto? = null,
    var categoryId: Long? = null,
    var description: String,
    var isIgnored: Boolean = false,
    var type: EntryType
) {
}