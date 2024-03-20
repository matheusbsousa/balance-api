package com.home.balance.api.controllers

import com.home.balance.api.services.SyncService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/sync-data")
class SyncController(@Autowired val syncService: SyncService) {


    @PostMapping
    fun syncData(): ResponseEntity<Unit> {
        syncService.syncData()
        return ResponseEntity.ok().build()
    }


}