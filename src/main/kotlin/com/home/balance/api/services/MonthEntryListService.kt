package com.home.balance.api.services

import com.home.balance.api.models.dtos.CategorizedEntriesDto
import com.home.balance.api.models.dtos.MonthEntryListDto

import com.home.balance.api.repositories.CategoryRepository
import com.home.balance.api.repositories.EntryRepository
import com.home.balance.api.utils.Constants.MONTHS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.util.*

@Service
class MonthEntryListService(
    @Autowired val entryRepository: EntryRepository,
    @Autowired val categoryRepository: CategoryRepository
) {

    fun getMonthEntryList(year: Int, showIgnored: Boolean): List<MonthEntryListDto> {

        val entries = entryRepository.findByYear(year, showIgnored)

        val entryDtoList = entries.map { it.toEntryDto() }.sortedBy { it.description }

        val monthEntryList = entryDtoList.groupBy { extractMonth(it.date) }
            .map { (key, value) ->
                MonthEntryListDto(month = key,
                    monthDescription = MONTHS[key - 1],
                    categorizedEntries =
                    value.groupBy { it.category!!.name }
                        .toSortedMap()
                        .map { (vKey, vValue) ->
                            CategorizedEntriesDto(
                                category = vKey,
                                total = vValue.sumOf { it.value },
                                entries = vValue
                            )
                        }
                )
            }.sortedBy { it.month }

        return monthEntryList
    }

    fun extractMonth(date: Date): Int {

        val entryDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

        if (entryDate.dayOfMonth < 5) {
            return entryDate.minusMonths(1).monthValue
        }

        return entryDate.monthValue
    }

}
