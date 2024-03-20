package com.home.balance.api.services


import com.home.balance.api.models.Entities.Entry
import com.home.balance.api.models.dtos.EntryDto
import com.home.balance.api.repositories.CategoryRepository
import com.home.balance.api.repositories.EntryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EntryService(
    @Autowired val entryRepository: EntryRepository,
    @Autowired val categoryRepository: CategoryRepository
) {

    fun createEntry(entryDto: EntryDto): EntryDto {
        val category =
            categoryRepository.findById(entryDto.categoryId!!).orElseThrow { RuntimeException("Category not found") }
        val entry = Entry(
            originalDescription = entryDto.description,
            description = entryDto.description,
            originalValue = entryDto.value,
            value = entryDto.value,
            originalDate = entryDto.date,
            date = entryDto.date,
            category = category,
            isCategorized = true
        )

        entryRepository.save(entry)
        return entry.toEntryDto()
    }

    fun updateEntry(entryDto: EntryDto, id: Long): EntryDto {
        val entry = entryRepository.findById(id).orElseThrow { RuntimeException("Entry not found") }
        val category =
            categoryRepository.findById(entryDto.categoryId!!).orElseThrow { RuntimeException("Category not found") }

        entry.date = entryDto.date
        entry.value = entryDto.value
        entry.category = category
        entry.description = entryDto.description
        entry.isIgnored = entryDto.isIgnored
        entry.isCategorized = true
        entryRepository.save(entry)
        return entry.toEntryDto()
    }

    fun deleteEntry(id: Long) {
        entryRepository.deleteById(id)
    }


}