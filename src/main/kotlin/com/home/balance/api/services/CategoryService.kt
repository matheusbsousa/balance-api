package com.home.balance.api.services

import com.home.balance.api.models.entities.Category
import com.home.balance.api.models.dtos.CategoryDto
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
                    if (!entry.isCategorized && (entry.description ?: entry.originalDescription).lowercase()
                            .contains(value.lowercase())
                    ) {
                        entry.category = categories.find { it.id == category.key }!!
                    }
                }
            }
        }

        entries.filter { it.category == null }.forEach { it.category = categories.find { it.name == "Desconhecido" }!! }

        entryRepository.saveAll(entries)
    }

    fun getCategories(): List<CategoryDto> {
        return categoryRepository.findAll().sortedBy { it.name }.map { it.toDto() }
    }

    fun saveCategory(cartegoryDto: CategoryDto) {

        categoryRepository.findCategoryByName(cartegoryDto.name)?.let {
            throw Exception("Category already exists")
        }

        val category = Category(
            name = cartegoryDto.name,
            values = cartegoryDto.values.joinToString(","),
            colorHex = cartegoryDto.colorHex
        )
        categoryRepository.save(category)
    }

    fun updateCategory(id: Long, cartegoryDto: CategoryDto) {

        categoryRepository.findById(id).orElseThrow {
            throw Exception("Category not found")
        }

        categoryRepository.findCategoryByName(cartegoryDto.name).let {
            if (it != null && it.id != id) {
                throw Exception("Category already exists")
            }
        }

        val category = Category(
            id = id,
            name = cartegoryDto.name,
            values = cartegoryDto.values.joinToString(","),
            colorHex = cartegoryDto.colorHex
        )
        categoryRepository.save(category)
    }

    fun deleteCategory(id: Long) {
        categoryRepository.findById(id).orElseThrow {
            throw Exception("Category not found")
        }

        val unknownCategory = categoryRepository.findCategoryByName("Desconhecido")!!

        val entries = entryRepository.findByCategoryId(id)

        entries.forEach {
            it.category = unknownCategory
        }

        entryRepository.saveAll(entries)
        categoryRepository.deleteById(id)
    }
}