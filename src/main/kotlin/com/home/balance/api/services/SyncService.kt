package com.home.balance.api.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SyncService(
    @Autowired val readerService: ReaderService,
    @Autowired val categoryService: CategoryService,
    private val entryService: EntryService,
) {

    fun syncData() {
        readerService.readDataFromFile()
        categoryService.categorize()
        entryService.findSplitPayments()
    }


}