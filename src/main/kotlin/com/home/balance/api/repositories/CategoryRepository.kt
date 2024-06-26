package com.home.balance.api.repositories

import com.home.balance.api.models.entities.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, Long> {
    fun findCategoryByName(name: String): Category?
}