package com.home.balance.api.services

import com.home.balance.api.models.dtos.EntryDto
import com.home.balance.api.models.entities.Entry
import com.home.balance.api.repositories.EntryRepository
import com.home.balance.api.utils.EntryUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SpreadsheetService(
    @Autowired val entryRepository: EntryRepository,
    private val categoryService: CategoryService,
) {

    fun saveEntries(entryDtoList: List<EntryDto>) {

        val entriesFromDatabase = entryRepository.findAll()
        val entryList = entryDtoList.map {
            Entry(
                originalValue = it.value,
                originalDate = it.date,
                date = it.date,
                originalDescription = EntryUtil().extractDescription(it.description)
            )
        }.toMutableList()

        val existentEntries = entryList.filter { entry ->
            entriesFromDatabase.any { databaseEntry ->
                entry.originalDate.toInstant() == databaseEntry.originalDate.toInstant()
                        && entry.originalDescription.lowercase() == databaseEntry.originalDescription.lowercase()
                        && entry.originalValue == databaseEntry.originalValue
            }
        }

        entryList.removeAll(existentEntries)
        entryRepository.saveAll(entryList)
        categoryService.categorize()
    }
}