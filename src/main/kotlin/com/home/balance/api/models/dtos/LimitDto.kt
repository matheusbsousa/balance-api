package com.home.balance.api.models.dtos

class LimitDto(
    var id: Long? = null,
    var description: String,
    var percentage: Double,
    var monthLimitId: Long? = null,
    var month : Int? = null,
    var year: Int? = null,
    var categoryIds: Set<Long>? = null,
    var categories: List<CategoryDto>? = null
) {
}