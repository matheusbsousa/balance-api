package com.home.balance.api.services


import com.home.balance.api.models.dtos.BalanceItemDto
import com.home.balance.api.models.dtos.CategoryBalanceDto
import com.home.balance.api.models.dtos.MonthBalanceDto
import com.home.balance.api.models.dtos.TotalBalanceDto
import com.home.balance.api.models.entities.Category
import com.home.balance.api.models.entities.Entry
import com.home.balance.api.models.entities.Limit
import com.home.balance.api.models.enums.EntryType
import com.home.balance.api.repositories.EntryRepository
import com.home.balance.api.repositories.MonthLimitRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BalanceService(
    @Autowired val entryRepository: EntryRepository,
    @Autowired val monthLimitRepository: MonthLimitRepository,
) {

    fun getMonthBalancesByYear(year: Int): List<MonthBalanceDto?> {

        val entries = entryRepository.findByYear(year, false)

        val monthEntries = entries.groupBy { it.month }

        val monthBalances = monthEntries.map { (month, entries) ->
            createMonthBalance(year, month, entries)
        }

        return monthBalances.sortedBy { it?.month }
    }

    private fun createMonthBalance(year: Int, month: Int, entries: List<Entry>): MonthBalanceDto? {

        return monthLimitRepository.findByYearAndMonth(year, month)?.let {
            return MonthBalanceDto(
                month = month,
                balanceItems = createBalanceItems(entries, it.limits)
                    .sortedBy { it.title }
            )
        }
    }

    private fun createBalanceItems(entries: List<Entry>, limits: List<Limit>?): List<BalanceItemDto> {

        val entryByCategory = entries.groupBy { it.category }
        val incomeTotalValue = entries.filter { it.type == EntryType.INCOME }.map { it.value }.sumOf { it }

        val balanceItems = mutableListOf<BalanceItemDto>()

        limits?.forEach { limit ->
            val limitCategories = limit.limitCategories?.map { it.category } ?: emptyList()

            val totalLimitEstimation = limit.limitCategories?.map { it.limitValue }?.sumOf { it }!!
            val totalLimitReal = entries.filter { limitCategories.contains(it.category) }.map { it.value }.sumOf { it }

            balanceItems.add(
                BalanceItemDto(
                    title = limit.description,
                    percentage = limit.percentage,
                    percentageValue = incomeTotalValue.times(limit.percentage).div(100),
                    balance = totalLimitEstimation - totalLimitReal,
                    categoryBalances = createCategoryBalances(entryByCategory, limit),
                    total = TotalBalanceDto(
                        real = totalLimitReal,
                        estimation = totalLimitEstimation
                    )
                )
            )
        }
        return balanceItems
    }

    private fun createCategoryBalances(
        entryByCategory: Map<Category, List<Entry>>,
        limit: Limit
    ): List<CategoryBalanceDto> {
        return limit.limitCategories?.map { limitCategory ->
            CategoryBalanceDto(
                description = limitCategory.category.name,
                estimation = limitCategory.limitValue,
                real = entryByCategory[limitCategory.category]?.map { it.value }?.sumOf { it } ?: 0.0
            )
        }?.sortedBy { it.description } ?: emptyList()
    }
}