package ru.practicum.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryCreateRequest;
import ru.practicum.dto.CategoryCreateResponse;
import ru.practicum.mappers.CategoryMapper;
import ru.practicum.models.Category;
import ru.practicum.services.CategoriesService;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Tag(name = "Admin: Категории")
public class AdminCategoriesController {

    private final CategoriesService service;
    private final CategoryMapper mapper;

    @PostMapping
    public ResponseEntity<CategoryCreateResponse> createCategory(@Valid @RequestBody CategoryCreateRequest createRequest) {
        Category category = service.createCategory(mapper.toCategory(createRequest));
        return new ResponseEntity<>(mapper.toCategoryResponse(category), HttpStatus.CREATED);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int catId) {
        service.deleteById(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryCreateResponse> updateCategory(@PathVariable int catId,
                                                                 @Valid @RequestBody CategoryCreateRequest request) {
        Category category = service.updateCategory(catId, mapper.toCategory(request));
        return new ResponseEntity<>(mapper.toCategoryResponse(category), HttpStatus.OK);
    }


}
