package com.home.balance.api.repositories

import com.home.balance.api.models.Entities.MonthLimit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MonthLimitRepository : JpaRepository<MonthLimit, Long> {
    fun findByYear(year: Int): List<MonthLimit>

    fun findByYearAndMonth(year: Int, month: Int): MonthLimit?
}


