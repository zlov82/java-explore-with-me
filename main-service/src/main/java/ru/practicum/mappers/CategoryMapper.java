package ru.practicum.mappers;

import org.mapstruct.Mapper;
import ru.practicum.dto.CategoryCreateRequest;
import ru.practicum.dto.CategoryCreateResponse;
import ru.practicum.models.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryCreateRequest createRequest);

    CategoryCreateResponse toCategoryResponse(Category category);

    List<CategoryCreateResponse> toCategoryResponse(List<Category> categoryList);
}
