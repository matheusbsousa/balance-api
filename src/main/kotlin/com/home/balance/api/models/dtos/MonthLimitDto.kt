package com.home.balance.api.models.dtos

class MonthLimitDto(
    var id: Long? = null,
    var monthDescription: String,
    var month: Int,
    var year: Int,
    var limits: List<LimitDto>? = null
) {
}