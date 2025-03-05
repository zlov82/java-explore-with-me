package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.models.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("""
            SELECT c
            FROM Category c
            ORDER BY id
            LIMIT :size
            OFFSET :from
            """)
    List<Category> selectCategoriesWithLimit(Integer from, Integer size);
}
