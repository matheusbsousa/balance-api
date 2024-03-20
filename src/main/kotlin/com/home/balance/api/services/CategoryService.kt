package com.home.balance.api.services

import com.home.balance.api.models.Entities.Category
import com.home.balance.api.repositories.CategoryRepository
import com.home.balance.api.repositories.EntryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CategoryService(
    @Autowired val categoryRepository: CategoryRepository,
    @Autowired val entryRepository: EntryRepository,
) {


    fun categorize() {
        val entries = entryRepository.findAll()
        val categories = categoryRepository.findAll()
        val categoryMap = categories.associate { it.id to it.values.split(",") }


        categoryMap.forEach { category ->
            category.value.forEach { value ->
                entries.forEach { entry ->
                    if (!entry.isCategorized && (entry.description ?: entry.originalDescription).contains(value)) {
                        entry.category = categories.find { it.id == category.key }!!
                    }
                }
            }
        }

        entries.filter { it.category == null }.forEach { it.category = categories.find { it.name == "Desconhecido" }!! }

        entryRepository.saveAll(entries)
    }

    fun getCategories(): List<Category> {
        return categoryRepository.findAll().sortedBy { it.name }
    }
}