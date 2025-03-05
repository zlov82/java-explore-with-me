package ru.practicum.repository;

import org.mapstruct.control.MappingControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.models.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
            SELECT u
            FROM User u
            ORDER BY id
            LIMIT :size
            OFFSET :from
            """)
    List<User> selectUsersWithLimit(Long from, Long size);

    @Query("""
            SELECT u
            FROM User u
            WHERE u.id IN (:ids)
            ORDER BY id
            LIMIT :size
            OFFSET :from
            """)
    List<User> selectUsersWithIds(List<Long> ids,Long from, Long size);
}
