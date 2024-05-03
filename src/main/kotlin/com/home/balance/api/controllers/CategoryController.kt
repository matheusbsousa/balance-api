package com.home.balance.api.controllers

import com.home.balance.api.models.dtos.CategoryDto
import com.home.balance.api.services.CategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/categories")
class CategoryController(@Autowired val categoryService: CategoryService) {

    @GetMapping
    fun getCategories(): ResponseEntity<List<CategoryDto>> {
        val result = categoryService.getCategories()
        return ResponseEntity.ok(result)
    }

    @PostMapping
    fun saveCategory(@RequestBody categoryDto: CategoryDto): ResponseEntity<Unit> {
        categoryService.saveCategory(categoryDto)
        return ResponseEntity.ok().build()
    }
    @PutMapping("/{id}")
    fun updateCategory(@PathVariable("id") id: Long, @RequestBody categoryDto: CategoryDto): ResponseEntity<Unit> {
        categoryService.updateCategory(id, categoryDto)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable("id") id: Long): ResponseEntity<Unit> {
        categoryService.deleteCategory(id)
        return ResponseEntity.ok().build()
    }
}