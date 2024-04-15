package com.home.balance.api.models.dtos

class MonthBalanceDto(
    var month: Int? = null,
    var balances: List<BalanceDto>,
    var income: Double? = null,
    var expense: Double? = null,
    var total: Double? = null
) {
}