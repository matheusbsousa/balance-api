package com.home.balance.api.services

import com.home.balance.api.models.dtos.CategoryDto
import com.home.balance.api.models.entities.Category
import com.home.balance.api.repositories.CategoryRepository
import com.home.balance.api.repositories.EntryRepository
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val entryRepository: EntryRepository,
    private val parametersService: ParametersService

) {

    fun categorize() {
        val entries = entryRepository.findAll()
        val unknownCategoryName = parametersService.getUnknownCategoryName()
        val categories = categoryRepository.findAll()
        val categoryMap = categories.associate { it.id to it.values.split(",") }


        categoryMap.forEach { category ->
            category.value.forEach { value ->
                entries.forEach { entry ->
                    if (!entry.isCategorized && entry.description.lowercase()
                            .contains(value.lowercase())
                    ) {
                        entry.category = categories.find { it.id == category.key }!!
                    }
                }
            }
        }

        entries.forEach { it.category = categories.find { it.name == unknownCategoryName }!! }

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

        val unknownCategoryName = parametersService.getUnknownCategoryName()
        val unknownCategory = categoryRepository.findCategoryByName(unknownCategoryName)!!

        val entries = entryRepository.findByCategoryId(id)

        entries.forEach {
            it.category = unknownCategory
        }

        entryRepository.saveAll(entries)
        categoryRepository.deleteById(id)
    }

    fun getUnknownCategory(): Category {
        val unknownCategoryName = parametersService.getUnknownCategoryName()
        return categoryRepository.findCategoryByName(unknownCategoryName)!!
    }
}