package com.home.balance.api.models.dtos

import java.util.*

class EntryDto(
    var id: Long? = null,
    var date: Date,
    var value: Double,
    var category: CategoryDto? = null,
    var categoryId: Long? = null,
    var description: String,
    var isIgnored: Boolean = false
) {
}