package ru.practicum.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.models.Subscribe;
import ru.practicum.models.User;
import ru.practicum.repository.SubscribeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscribeService {
    private final SubscribeRepository repository;
    private final UserService userService;

    public Subscribe create(Long userId, Long followerId) {
        if (userId.equals(followerId)) {
            throw new ConflictException("Нельзя добавить себя в друзья");
        }
        User user = userService.getUserById(userId);
        User follower = userService.getUserById(followerId);
        Subscribe subscribe = Subscribe.builder()
                .user(user)
                .follower(follower)
                .build();

        return repository.save(subscribe);
    }

    public List<Subscribe> getAll(Long userId) {
        User user = userService.getUserById(userId);

        return repository.findAllByUser(user);
    }

    public void delete(Long userId, Long followerId) {
        User user = userService.getUserById(userId);
        User follower = userService.getUserById(followerId);

        Subscribe subscribe = repository.findByUserAndFollower(user, follower).orElseThrow(
                () -> new NotFoundException("Не найдена подписка пользователя id=" + userId + " на пользователя id=" + followerId)
        );

        repository.delete(subscribe);
    }
}
