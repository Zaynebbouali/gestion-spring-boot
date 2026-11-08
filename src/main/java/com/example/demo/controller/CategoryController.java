package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Category;
import com.example.demo.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Category ajouterCategorie(@RequestBody Category category) {
        return categoryService.ajouterCategorie(category);
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        return categoryService.trouverCategorieParId(id);
    }

    @PutMapping("/{id}")
    public Category modifierCategorie(@PathVariable Long id,
                                      @RequestBody Category categoryDetails) {
        return categoryService.modifierCategorie(id, categoryDetails);
    }

    @DeleteMapping("/{id}")
    public Long supprimerCategorie(@PathVariable Long id) {
        return categoryService.supprimerCategorie(id);
    }
}