package com.home.balance.api.models.dtos

class MonthBalanceDto(
    var month: Int,
    var balanceItems: List<BalanceItemDto>,
) {
}