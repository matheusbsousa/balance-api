package com.home.balance.api.services


import com.home.balance.api.models.entities.Entry
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
        val entry = entryRepository.findById(id).orElseThrow { RuntimeException("Entry not found") }
        entry.isIgnored = true
        entryRepository.save(entry)
    }

    fun findSplitPayments() {
        var entries = entryRepository.findAll()

        val filtered = entries.filter({ it.originalDescription.contains(Regex("\\(\\d+\\/\\d+\\)")) })

        filtered.forEach { entry ->
            if (entry.originalDescription.contains(Regex("\\((0?1)/\\d+\\)"))) {
                if (entry.description != null) {
                    val description = entry.description!!
                        .replace(entry.originalDescription, "")
                        .replace(Regex("\\((0?1)/\\d+\\)"), "")
                        .replace("-", "")
                        .replace("**", "")
                        .replace("*-*", "")
                        .trim()
                    entry.description = entry.originalDescription + " *-* " + description
                }
            }
        }

        filtered.forEach { entry ->
            if (entry.originalDescription.contains(Regex("\\((?!0?1\\b)(\\d+)\\/(?!0?1\\b)(\\d+)\\)"))) {
                val matchResult = Regex("\\((\\d+)/(\\d+)\\)").find(entry.originalDescription)
                val firstNumber = matchResult?.groups?.get(1)?.value?.padStart(2, '0')
                val secondNumber = matchResult?.groups?.get(2)?.value?.padStart(2, '0')
                val previousEntry = entry.originalDescription.replace(
                    Regex("\\(\\d+\\/\\d+\\)"),
                    "(${firstNumber?.toInt()!!.minus(1).toString().padStart(2, '0')}/${secondNumber})"
                )

                filtered.find { it.originalDescription.equals(previousEntry)
                        && it.originalValue.equals(entry.originalValue)
                }
                    ?.let {
                        entry.description = it.description?.replace(
                            Regex("\\(\\d+\\/\\d+\\)"),
                            "(${firstNumber}/$secondNumber)"
                        )

                        entry.category = it.category
                    }
            }
        }

        entryRepository.saveAll(filtered)
    }


}