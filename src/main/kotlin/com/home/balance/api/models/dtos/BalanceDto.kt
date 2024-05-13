package com.home.balance.api.models.dtos

class BalanceDto(
    var description: String,
    var percentage: Double,
    var categories: List<CategoryBalanceDto>,
    var limitValue: Double,
    var limitTotal: Double,
    var total: Double
) {

}
