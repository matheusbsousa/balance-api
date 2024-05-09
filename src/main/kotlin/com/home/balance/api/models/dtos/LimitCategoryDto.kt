package com.home.balance.api.models.dtos

class LimitCategoryDto(
    var id: Long? = null,
    var categoryId: Long,
    var description: String,
    var limit: Double = 0.0,
) {
}