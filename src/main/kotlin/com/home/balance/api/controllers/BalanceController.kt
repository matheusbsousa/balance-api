package com.home.balance.api.controllers

import com.home.balance.api.models.dtos.MonthBalanceDto
import com.home.balance.api.services.BalanceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/balances")
class BalanceController(@Autowired val balanceService: BalanceService) {

    @GetMapping
    fun getBalance(@RequestParam("year") year: Int): ResponseEntity<List<MonthBalanceDto>> {
        val result = balanceService.getMonthBalance(year)
        return ResponseEntity.ok(result)
    }
}