package com.home.balance.api.controllers

import com.home.balance.api.models.dtos.MonthLimitDto
import com.home.balance.api.services.LimitService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/month-limits")
class MonthLimitController(@Autowired val limitService: LimitService) {

    @GetMapping
    fun getLimits(@RequestParam("year") year: Int): ResponseEntity<List<MonthLimitDto>> {
        val result = limitService.getLimits(year)
        return ResponseEntity.ok(result)
    }

    @PostMapping
    fun createMonthLimit(@RequestBody monthLimitDto: MonthLimitDto): ResponseEntity<MonthLimitDto> {
        var result = limitService.createMonthLimit(monthLimitDto)
        return ResponseEntity.ok(result)
    }
}