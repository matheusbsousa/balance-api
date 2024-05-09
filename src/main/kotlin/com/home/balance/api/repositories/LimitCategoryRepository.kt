package com.home.balance.api.repositories

import com.home.balance.api.models.entities.LimitCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LimitCategoryRepository : JpaRepository<LimitCategory, Long>{
    fun findByLimitId(id: Long): List<LimitCategory>
}


