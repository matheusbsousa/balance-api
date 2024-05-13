package com.home.balance.api.services


import com.home.balance.api.models.dtos.BalanceDto
import com.home.balance.api.models.dtos.CategoryBalanceDto
import com.home.balance.api.models.dtos.MonthBalanceDto
import com.home.balance.api.repositories.EntryRepository
import com.home.balance.api.repositories.MonthLimitRepository
import com.home.balance.api.utils.Constants.SALARIO_CATEGORY
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.util.*

@Service
class BalanceService(
    @Autowired val entryRepository: EntryRepository,
    @Autowired val monthLimitRepository: MonthLimitRepository
) {


    fun getMonthBalance(year: Int): List<MonthBalanceDto> {

        val entries = entryRepository.findByYear(year, false)
        val monthLimit = monthLimitRepository.findByYear(year)


        return monthLimit.map {
            MonthBalanceDto(
                month = it.month,
                balances = it.limits!!.map { limit ->
                    BalanceDto(
                        description = limit.description,
                        percentage = limit.percentage,
                        categories = limit.limitCategories!!.map { limitCategory ->
                            CategoryBalanceDto(
                                description = limitCategory.category.name,
                                limit = limitCategory.limitValue ?: 0.0,
                                value = entries
                                    .filter { entry ->
                                        extractDate(entry.date ?: entry.originalDate) == it.month
                                                && entry.category!!.id == limitCategory.category.id
                                    }.sumOf { it.value ?: it.originalValue }
                            )
                        },
                        limitValue = entries.filter { entry -> extractDate(entry.date!!) == it.month && entry.category!!.name == SALARIO_CATEGORY }
                            .sumOf { it.value ?: it.originalValue }
                            .times(limit.percentage)
                            .div(100),
                        limitTotal =  limit.limitCategories!!.map{it.limitValue}.sumOf { it ?: 0.0 },
                        total = entries
                            .filter { entry -> extractDate(entry.date!!) == it.month
                                    && limit.limitCategories!!.map { it.category }.contains(entry.category)
                            }
                            .sumOf { it.value ?: it.originalValue }
                    )
                },
            )
        }
    }

    fun extractDate(date: Date): Int {

        val entryDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

        if (entryDate.dayOfMonth < 5) {
            return entryDate.monthValue - 1
        }
        return entryDate.monthValue
    }

}