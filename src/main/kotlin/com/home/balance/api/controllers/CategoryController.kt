package com.home.balance.api.controllers

import com.home.balance.api.models.Entities.Category
import com.home.balance.api.services.CategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/categories")
class CategoryController(@Autowired val categoryService: CategoryService) {

    @GetMapping
    fun getCategories(): ResponseEntity<List<Category>> {
        val result = categoryService.getCategories()
        return ResponseEntity.ok(result)
    }
}