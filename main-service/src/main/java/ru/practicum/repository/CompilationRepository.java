package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.models.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query("""
            SELECT c
            FROM Compilation c
            WHERE 1=1
            AND c.pinned = :pinned
            ORDER BY c.id
            LIMIT :size
            OFFSET :from
            """)
    List<Compilation> findByPinnedWithFromAndSize(boolean pinned, int from, int size);

    @Query("""
            SELECT c
            FROM Compilation c
            WHERE 1=1
            ORDER BY c.id
            LIMIT :size
            OFFSET :from
            """)
    List<Compilation> findAllWithFromAndSize(int from, int size);
}
