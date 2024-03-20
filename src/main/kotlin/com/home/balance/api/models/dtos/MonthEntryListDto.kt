package com.home.balance.api.models.dtos

class MonthEntryListDto(
    var monthDescription: String? = null,
    var month: Int? = null,
    var categorizedEntries: List<CategorizedEntriesDto>,
)