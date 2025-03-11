package ru.practicum.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryCreateResponse;
import ru.practicum.mappers.CategoryMapper;
import ru.practicum.models.Category;
import ru.practicum.services.CategoriesService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Public: Категории")
public class PublicCategoriesController {

    private final CategoriesService service;
    private final CategoryMapper mapper;

    @GetMapping
    public ResponseEntity<List<CategoryCreateResponse>> getAll(@RequestParam(required = false, defaultValue = "0") Integer from,
                                                               @RequestParam(required = false, defaultValue = "10") Integer size) {
        List<Category> categoryList = service.getCategories(from, size);
        return new ResponseEntity<>(mapper.toCategoryResponse(categoryList), HttpStatus.OK);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryCreateResponse> getOne(@PathVariable Integer catId) {
        Category category = service.getCategoryById(catId);
        return new ResponseEntity<>(mapper.toCategoryResponse(category), HttpStatus.OK);
    }
}
