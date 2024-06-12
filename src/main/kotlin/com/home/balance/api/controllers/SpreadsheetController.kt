package com.home.balance.api.controllers

import com.home.balance.api.models.dtos.EntryDto
import com.home.balance.api.services.SpreadsheetService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/spreadsheet")
class SpreadsheetController(@Autowired val spreadsheetService: SpreadsheetService) {

    @PostMapping
    fun saveEntries(@RequestBody entryDtoList: List<EntryDto>): ResponseEntity<Void> {
        spreadsheetService.saveEntries(entryDtoList)
        return ResponseEntity.ok().build()
    }
}