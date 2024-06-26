package com.home.balance.api.controllers

import com.home.balance.api.models.dtos.LimitCategoryDto
import com.home.balance.api.models.dtos.LimitDto
import com.home.balance.api.services.LimitService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/limits")
class LimitController(@Autowired val limitService: LimitService) {

    @PostMapping
    fun createLimit(
        @RequestBody limit: LimitDto
    ): ResponseEntity<LimitDto> {
        val result = limitService.createLimit(limit)
        return ResponseEntity.ok(result)
    }

    @PutMapping("/{limitId}")
    fun updateLimit(
        @RequestBody limit: LimitDto,
        @PathVariable("limitId") limitId: Long
    ): ResponseEntity<LimitDto> {
        val result = limitService.updateLimit(limit, limitId)
        return ResponseEntity.ok(result)
    }

    @DeleteMapping("/{limitId}")
    fun deleteLimit(
        @PathVariable("limitId") limitId: Long
    ): ResponseEntity<Unit> {
        limitService.deleteLimit(limitId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/copy-last-month")
    fun copyLastMonthLimits(
        @RequestParam year: Int,
        @RequestParam month: Int
    ): ResponseEntity<Unit> {
        limitService.copyLastMonthLimits(year, month)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/{limitId}/categories")
    fun updateLimitCategories(
        @RequestBody limitCategories: List<LimitCategoryDto>,
        @PathVariable("limitId") limitId: Long
    ): ResponseEntity<Void> {
        limitService.updateLimitCategories(limitCategories, limitId)
        return ResponseEntity.ok().build()
    }
}