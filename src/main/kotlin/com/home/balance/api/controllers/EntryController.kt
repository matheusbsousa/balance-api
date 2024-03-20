package com.home.balance.api.controllers


import com.home.balance.api.models.dtos.EntryDto
import com.home.balance.api.services.EntryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/entries")
class EntryController(@Autowired val entryService: EntryService) {

    @PostMapping
    fun createEntry(@RequestBody entryDto: EntryDto): ResponseEntity<EntryDto> {
        val result = entryService.createEntry(entryDto)
        return ResponseEntity.ok(result)
    }

    @PutMapping("/{id}")
    fun updateEntry(
        @RequestBody entryDto: EntryDto,
        @PathVariable("id") id: Long
    ): ResponseEntity<EntryDto> {
        val result = entryService.updateEntry(entryDto, id)
        return ResponseEntity.ok(result)
    }

    @DeleteMapping("/{id}")
    fun deleteEntry(@PathVariable("id") id: Long): ResponseEntity<Unit> {
        entryService.deleteEntry(id)
        return ResponseEntity.noContent().build()
    }

}