package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.models.Subscribe;
import ru.practicum.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    List<Subscribe> findAllByUser(User user);

    Optional<Subscribe> findByUserAndFollower(User user, User follower);
}
