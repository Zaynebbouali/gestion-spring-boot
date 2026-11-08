package com.example.demo.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Entity.Category;
import com.example.demo.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // AJOUTER
    @PostMapping
    public Category ajouterCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    // LISTER
    @GetMapping
    public List<Category> listerCategories() {
        return categoryService.getAllCategories();
    }

    // AFFICHER UNE CATEGORIE
    @GetMapping("/{id}")
    public Category afficherCategory(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    // MODIFIER
    @PutMapping("/{id}")
    public Category modifierCategory(
            @PathVariable Long id,
            @RequestBody Category categoryDetails) {
        return categoryService.updateCategory(id, categoryDetails);
    }

    // SUPPRIMER
    @DeleteMapping("/{id}")
    public Long supprimerCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }
}