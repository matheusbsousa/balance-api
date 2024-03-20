package com.home.balance.api.controllers


import com.home.balance.api.models.dtos.MonthEntryListDto
import com.home.balance.api.services.MonthEntryListService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/month-entries")
class MonthEntryListController(@Autowired val monthEntryListService: MonthEntryListService) {

    @GetMapping
    fun getMonthEntryList(
        @RequestParam("year") year: Int,
        @RequestParam("showIgnored") showIgnored: Boolean,
        @RequestParam("categoryId") categoryId: Long? = null
    ): ResponseEntity<List<MonthEntryListDto>> {
        val result = monthEntryListService.getMonthEntryList(year, showIgnored)
        return ResponseEntity.ok(result)
    }
}