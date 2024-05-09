package com.home.balance.api.repositories

import com.home.balance.api.models.entities.Entry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EntryRepository : JpaRepository<Entry, Long> {
    @Query(value = "select entry from Entry entry " +
            " where year(entry.date) = :year " +
            " and (entry.isIgnored = :showIgnored or entry.isIgnored = false) ")
    fun findByYear(@Param("year") year: Int,
                   @Param("showIgnored") showIgnored: Boolean): List<Entry>

    @Query(value = "select e from Entry e where e.category.id = :id")
    fun findByCategoryId(id: Long): List<Entry>
}