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
        User savedUser = this.getUser(userId);
        repository.delete(savedUser);
    }

    public List<User> getUsers(List<Long> ids, Long from, Long size) {
        if (ids == null || ids.isEmpty()) {
            return repository.selectUsersWithLimit(from, size);
        }
        return repository.selectUsersWithIds(ids, from, size);
    }

    public User getUserById(long id) {
        return this.getUser(id);
    }

    private User getUser(long id) {
        Optional<User> optUser = repository.findById(id);
        if (optUser.isEmpty()) {
            throw new NotFoundException("User with id=" + id + " was not found");
        }
        return optUser.get();
    }

    public List<User> getUsersByIds(List<Long> ids) {
        return repository.findAllById(ids);
    }
}
