package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.models.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
