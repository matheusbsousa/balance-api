package com.home.balance.api.models.dtos

data class CategoryBalanceDto(
    var description: String,
    var estimation: Double,
    var real: Double
) {

}
