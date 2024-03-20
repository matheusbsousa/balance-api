package com.home.balance.api.services

import com.home.balance.api.models.Entities.Limit
import com.home.balance.api.models.Entities.MonthLimit
import com.home.balance.api.models.dtos.LimitDto
import com.home.balance.api.models.dtos.MonthLimitDto
import com.home.balance.api.repositories.CategoryRepository
import com.home.balance.api.repositories.LimitRepository
import com.home.balance.api.repositories.MonthLimitRepository
import com.home.balance.api.utils.Constants.MONTHS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LimitService(
    @Autowired val limitRepository: LimitRepository,
    @Autowired val monthRepository: MonthLimitRepository,
    @Autowired val categoryRepository: CategoryRepository
) {

    fun getLimits(year: Int): List<MonthLimitDto> {
        val monthLimits = monthRepository.findByYear(year)
        return monthLimits.map { it.toDto() }
    }


    fun createMonthLimit(monthLimitDto: MonthLimitDto): MonthLimitDto {
        val monthLimit = MonthLimit(
            month = monthLimitDto.month,
            monthDescription = monthLimitDto.monthDescription,
            year = monthLimitDto.year
        )

        monthRepository.save(monthLimit)
        return monthLimit.toDto()
    }

    fun createLimit(limitDto: LimitDto): LimitDto {

        val monthLimit = if (limitDto.monthLimitId != null) {
            monthRepository.findById(limitDto.monthLimitId!!)
                .orElseThrow { throw RuntimeException("Month limit not found") }
        } else {
            monthRepository.save(
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
            categories = categories,
            monthLimit = monthLimit,
            percentage = limitDto.percentage
        )

        limitRepository.save(limit)

        return limit.toDto()
    }

    fun updateLimit(limitDto: LimitDto, limitId: Long): LimitDto {

        val limit = limitRepository.findById(limitId)
            .orElseThrow { throw RuntimeException("Limit not found") }

        val categories = categoryRepository.findAllById(limitDto.categoryIds!!)

        if (limitDto.categoryIds!!.size > categories.size) {
            throw RuntimeException("Some categories not found")
        }

        limit.description = limitDto.description
        limit.categories = categories
        limit.percentage = limitDto.percentage

        limitRepository.save(limit)

        return limit.toDto()
    }

    fun deleteLimit(limitId: Long) {
        val limit = limitRepository.findById(limitId)
            .orElseThrow { throw RuntimeException("Limit not found") }

        limitRepository.delete(limit)
    }
}