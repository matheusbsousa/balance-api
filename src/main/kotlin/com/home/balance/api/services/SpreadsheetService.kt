package com.home.balance.api.services

import com.home.balance.api.models.dtos.EntryDto
import com.home.balance.api.models.entities.Entry
import com.home.balance.api.models.enums.EntryType
import com.home.balance.api.repositories.EntryRepository
import com.home.balance.api.repositories.ParametersRepository
import com.home.balance.api.utils.DateUtil.Companion.getMonth
import com.home.balance.api.utils.EntryUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SpreadsheetService(
    private val entryRepository: EntryRepository,
    private val categoryService: CategoryService,
    private val parametersService: ParametersService
) {

    fun saveEntries(entryDtoList: List<EntryDto>) {

        val firstDayOfTheMonth = parametersService.getFirstDayOfTheMonth()
        val unknownCategory = categoryService.getUnknownCategory()

        val entriesFromDatabase = entryRepository.findAll()
        val entryList = entryDtoList.map {
            Entry(
                description = EntryUtil().extractDescription(it.description),
                value = it.value,
                date = it.date,
                originalDescription = EntryUtil().extractDescription(it.description),
                originalValue = it.value,
                originalDate = it.date,
                month = getMonth(firstDayOfTheMonth, it.date),
                category = unknownCategory,
                type = EntryType.EXPENSE
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