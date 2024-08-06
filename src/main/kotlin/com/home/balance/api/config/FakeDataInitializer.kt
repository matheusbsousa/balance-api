package com.home.balance.api.config

import com.home.balance.api.models.entities.Category
import com.home.balance.api.models.entities.Entry
import com.home.balance.api.models.entities.Limit
import com.home.balance.api.models.entities.LimitCategory
import com.home.balance.api.models.entities.MonthLimit
import com.home.balance.api.models.enums.EntryType
import com.home.balance.api.repositories.CategoryRepository
import com.home.balance.api.repositories.EntryRepository
import com.home.balance.api.repositories.LimitCategoryRepository
import com.home.balance.api.repositories.LimitRepository
import com.home.balance.api.repositories.MonthLimitRepository
import com.home.balance.api.services.SyncService
import com.home.balance.api.utils.DateUtil
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.util.*

@Profile("local")
@Configuration
class FakeDataInitializer(
    private val entryRepository: EntryRepository,
    private val categoryRepository: CategoryRepository,
    private val monthLimitRepository: MonthLimitRepository,
    private val syncService: SyncService,
    private val limitRepository: LimitRepository,
    private val limitCategoryRepository: LimitCategoryRepository,
) {

    init {
        seed()
    }

    private fun seed() {
        generateCategories()
        generateMonthLimit()
        generateLimits()
        generateEntries()
        syncService.syncData()
    }


    private fun generateLimits() {

        val limits = mutableListOf<Limit>()
        val date = DateUtil.toLocalDate(Date())
        val categories = categoryRepository.findAll()
        val monthLimit = monthLimitRepository.findByYearAndMonth(date.year, date.monthValue)

        for (i in 1..10) {
            limits.add(
                Limit(
                    percentage = Random().nextInt(1, 100).toDouble(),
                    description = "Limit $i",
                    monthLimit = monthLimit!!,
                )
            )
        }

        limitRepository.saveAll(limits)

        val limitCategories = mutableListOf<LimitCategory>()
        for (limit in limits) {
            limitCategories.add(
                LimitCategory(
                    limit = limit,
                    category = categories.random()
                )
            )
        }

        limitCategoryRepository.saveAll(limitCategories)
    }

    private fun generateMonthLimit() {
        val date = DateUtil.toLocalDate(Date())
        val monthLimit = MonthLimit(
            monthDescription = date.month.name,
            month = date.monthValue,
            year = date.year,
        )
        monthLimitRepository.save(monthLimit)
    }

    private fun generateCategories() {
        val categories = mutableListOf<Category>()

        for (i in 1..10) {
            categories.add(
                Category(
                    name = "Category $i",
                    values = "Entry $i",
                    colorHex = "#a2a2a2"
                )
            )
        }

        categoryRepository.saveAll(categories)
    }

    private fun generateEntries() {

        val entries = mutableListOf<Entry>()
        val categories = categoryRepository.findAll()

        for (i in 1..20) {
            entries.add(
                Entry(
                    description = "Entry $i",
                    value = Random().nextInt(1, 100).toDouble(),
                    date = Date(),
                    originalDescription = "Entry $i",
                    originalValue = Random().nextInt(1, 100).toDouble(),
                    originalDate = Date(),
                    category = categories[Random().nextInt(1, 10)],
                    isCategorized = true,
                    month = DateUtil.toLocalDate(Date()).monthValue,
                    type = if (i % 2 == 0) EntryType.EXPENSE else EntryType.INCOME
                )
            )
        }
        entryRepository.saveAll(entries)
    }

}