package com.home.balance.api.models.dtos

data class BalanceItemDto(
    val title: String,
    val balance: Double,
    val percentage: Double,
    val percentageValue: Double,
    val categoryBalances: List<CategoryBalanceDto>,
    val total: TotalBalanceDto,
) {

}
