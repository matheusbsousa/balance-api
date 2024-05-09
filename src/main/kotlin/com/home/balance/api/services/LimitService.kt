package com.home.balance.api.services

import com.home.balance.api.models.dtos.LimitCategoryDto
import com.home.balance.api.models.dtos.LimitDto
import com.home.balance.api.models.dtos.MonthLimitDto
import com.home.balance.api.models.entities.Limit
import com.home.balance.api.models.entities.LimitCategory
import com.home.balance.api.models.entities.MonthLimit
import com.home.balance.api.repositories.CategoryRepository
import com.home.balance.api.repositories.LimitCategoryRepository
import com.home.balance.api.repositories.LimitRepository
import com.home.balance.api.repositories.MonthLimitRepository
import com.home.balance.api.utils.Constants.MONTHS
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LimitService(
    @Autowired val limitRepository: LimitRepository,
    @Autowired val monthLimitRepository: MonthLimitRepository,
    @Autowired val categoryRepository: CategoryRepository,
    @Autowired val limitCategoriesRepository: LimitCategoryRepository
) {

    fun getLimits(year: Int): List<MonthLimitDto> {
        val monthLimits = monthLimitRepository.findByYear(year)
        return monthLimits.map { it.toDto() }
    }


    fun createMonthLimit(monthLimitDto: MonthLimitDto): MonthLimitDto {
        val monthLimit = MonthLimit(
            month = monthLimitDto.month,
            monthDescription = monthLimitDto.monthDescription,
            year = monthLimitDto.year
        )

        monthLimitRepository.save(monthLimit)
        return monthLimit.toDto()
    }

    fun createLimit(limitDto: LimitDto): LimitDto {

        val monthLimit = if (limitDto.monthLimitId != null) {
            monthLimitRepository.findById(limitDto.monthLimitId!!)
                .orElseThrow { throw RuntimeException("Month limit not found") }
        } else {
            monthLimitRepository.save(
                MonthLimit(
                    month = limitDto.month!!,
                    monthDescription = MONTHS[limitDto.month!! - 1],
                    year = limitDto.year!!
                )
            )
        }

        val categories = categoryRepository.findAllById(limitDto.categoryIds!!)

        if (categories.size > limitDto.categoryIds!!.size) {
            throw RuntimeException("Some categories not found")
        }

        val limit = Limit(
            description = limitDto.description,
            monthLimit = monthLimit,
            percentage = limitDto.percentage
        )

        limitRepository.save(limit)

        val limitCategories = categories.map { category ->
            LimitCategory(
                category = category,
                limit = limit
            )
        }

        limitCategoriesRepository.saveAll(limitCategories)

        return limit.toDto()
    }

    fun updateLimit(limitDto: LimitDto, limitId: Long): LimitDto {

        val limit = limitRepository.findById(limitId)
            .orElseThrow { throw RuntimeException("Limit not found") }

        val categories = categoryRepository.findAllById(limitDto.categoryIds!!)

        if (limitDto.categoryIds!!.size > categories.size) {
            throw RuntimeException("Some categories not found")
        }

        val limitCategories = limitCategoriesRepository.findByLimitId(limitId)

        val limitCategoriesToDelete = limitCategories.filter { !categories.contains(it.category) }.toList()
        limitCategoriesRepository.deleteAll(limitCategoriesToDelete)

        val newLimitCategories =
            categories.filter { category -> !limitCategories.map { it.category }.contains(category) }
                .map { category -> LimitCategory(category = category, limit = limit) }

        limitCategoriesRepository.saveAll(newLimitCategories)

        limit.description = limitDto.description
        limit.percentage = limitDto.percentage

        limitRepository.save(limit)

        return limit.toDto()
    }

    fun deleteLimit(limitId: Long) {
        val limit = limitRepository.findById(limitId)
            .orElseThrow { throw RuntimeException("Limit not found") }

        limitRepository.delete(limit)
    }

    //    TODO Fix this
    @Transactional(rollbackOn = [Exception::class])
    fun copyLastMonthLimits(year: Int, month: Int) {

        val lastMonth = if (month == 1) {
            monthLimitRepository.findByYearAndMonth(year - 1, 12)
        } else {
            monthLimitRepository.findByYearAndMonth(year, month - 1)
        }

        var monthLimit = monthLimitRepository.findByYearAndMonth(year, month)

        if (monthLimit == null) {
            monthLimit = MonthLimit(
                month = month,
                monthDescription = MONTHS[month - 1],
                year = year
            )
            monthLimitRepository.save(monthLimit)
        }

        if (lastMonth?.limits != null) {
            val limits = lastMonth.limits!!.map { limit ->
                Limit(
                    description = limit.description,
                    monthLimit = monthLimit,
                    percentage = limit.percentage
                )
            }
            limitRepository.saveAll(limits)

            lastMonth.limits?.filter { it.limitCategories != null }?.let {

                it.forEach { lastMonthLimit ->
                    limits.find { limit -> limit.description.equals(lastMonthLimit.description) }?.let { limit ->
                        limit.limitCategories = lastMonthLimit.limitCategories!!.map { limitCategory ->
                            LimitCategory(
                                category = limitCategory.category,
                                limitValue = limitCategory.limitValue,
                                limit = limit
                            )
                        }
                    }
                }

                limitCategoriesRepository.saveAll(limits.flatMap { it.limitCategories!! })
            }
        }
    }

    fun updateLimitCategories(limitCategories: List<LimitCategoryDto>, limitId: Long) {
        val limit = limitRepository.findById(limitId)
            .orElseThrow { throw RuntimeException("Limit not found") }

        limit.limitCategories!!.forEach { limitCategory ->
            limitCategories.find { it.id!!.equals(limitCategory.id) }?.let {
                limitCategory.limitValue = it.limit
            }
        }

        limitCategoriesRepository.saveAll(limit.limitCategories!!)
    }
}