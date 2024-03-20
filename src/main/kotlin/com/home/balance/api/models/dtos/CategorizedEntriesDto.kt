package com.home.balance.api.models.dtos

class CategorizedEntriesDto (
    var category: String,
    var entries: List<EntryDto>,
    var total: Double
){
}