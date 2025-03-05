package ru.practicum.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.models.Category;
import ru.practicum.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriesService {

    private final CategoryRepository repository;

    public Category createCategory(Category category) {
        return repository.save(category);
    }

    public void deleteById(int catId) {
         Category category = this.getCategory(catId);
         repository.delete(category);
    }

    public Category updateCategory(int catId, Category category) {
        Category savedCategory = this.getCategory(catId);
        savedCategory.setName(category.getName());
        return repository.save(savedCategory);
    }

    public Category getCategoryById(int catId) {
        return this.getCategory(catId);
    }

    public List<Category> getCategories(int from, int size) {
        return repository.selectCategoriesWithLimit(from, size);
    }

    private Category getCategory(int id) {
        Optional<Category> savedCategory = repository.findById(id);
        if (savedCategory.isEmpty()) {
            throw new NotFoundException("Не найдена категория с id=" + id);
        }
        return savedCategory.get();
    }
}
