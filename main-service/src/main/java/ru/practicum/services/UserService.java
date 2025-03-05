package ru.practicum.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.models.User;
import ru.practicum.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User registerUser(User user) {
        return repository.save(user);
    }

    public void deleteUser(long userId) {
        Optional<User> userToDelete = repository.findById(userId);
        if (userToDelete.isEmpty()) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }
        repository.delete(userToDelete.get());
    }

    public List<User> getUsers(List<Long> ids, Long from, Long size) {
        if (ids == null || ids.isEmpty()) {
            return repository.selectUsersWithLimit(from, size);
        }
        return repository.selectUsersWithIds(ids, from, size);
    }
}
