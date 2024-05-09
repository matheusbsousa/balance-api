package com.home.balance.api.services

import com.home.balance.api.models.entities.Entry
import com.home.balance.api.repositories.CategoryRepository
import com.home.balance.api.repositories.EntryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat


@Service
class ReaderService(
    @Autowired val entryRepository: EntryRepository,
    @Autowired val categoryRepository: CategoryRepository) {

    fun readDataFromFile() {
        readEntries()
    }

    private fun readEntries() {

        val folder = File("src/main/resources/entries")

        if (folder.exists() && folder.isDirectory) {
            folder.listFiles()?.forEach { file ->
                val reader = file.bufferedReader()

                val entriesFromDatabase = entryRepository.findAll()

                val entryList = reader.lineSequence()
                    .filter { it.trim(',').isNotBlank() }
                    .drop(1)
                    .map {
                        val (date, description, value) = it.split(",", ignoreCase = true, limit = 3)
                        Entry(
                            id = null,
                            originalDate = SimpleDateFormat("dd/MM/yyyy").parse(date),
                            date = SimpleDateFormat("dd/MM/yyyy").parse(date),
                            originalDescription = extractDescription(description),
                            originalValue = extractDouble(value)
                        )
                    }
                    .toMutableList()

                reader.close()

                val existentEntries = entryList.filter { entry ->
                    entriesFromDatabase.any { databaseEntry ->
                        entry.originalDate.toInstant() == databaseEntry.originalDate.toInstant()
                                && entry.originalDescription.lowercase() == databaseEntry.originalDescription.lowercase()
                                && entry.originalValue == databaseEntry.originalValue
                    }
                }

                entryList.removeAll(existentEntries)

                entryRepository.saveAll(entryList)
            }
        }

    }

    private fun extractDescription(description: String): String {
        return description.trim()
            .replace("_", " ")
            .lowercase()
            .replaceFirstChar(Char::titlecase)
    }

    private fun getFile(file: String): InputStream {
        return this::class.java.getResourceAsStream(file) ?: throw IllegalArgumentException("File $file not found")

    }

    private fun extractDouble(string: String): Double {
        if (string.isNotEmpty()) {
            return string
                .replace("\"", "")
                .replace("R$", "")
                .replace("(", "")
                .replace(")", "")
                .replace(",", "").toDouble()
        }

        return 0.0
    }
}