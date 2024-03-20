package com.home.balance.api.models.dtos

class UpdateBalanceItemDto(
    var description: String,
    var value: Double,
    var categoryId: Long,
    var isIgnored: Boolean,
)