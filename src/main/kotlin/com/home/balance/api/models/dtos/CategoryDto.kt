package com.home.balance.api.models.dtos

class CategoryDto(
    var id: Long? = null,
    var name: String,
    var values: List<String>,
    var colorHex: String? = null
)