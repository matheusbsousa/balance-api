package com.home.balance.api.models.dtos

class MonthBalanceDto(
    var month: Int? = null,
    var balances: List<BalanceDto>,
) {
}