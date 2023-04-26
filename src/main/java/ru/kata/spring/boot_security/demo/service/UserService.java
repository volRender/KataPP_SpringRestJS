package ru.kata.spring.boot_security.demo.service;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Service
public interface UserService {
    public List<User> allUsers();
    public void addOrUpdateUser(User user);
    public void setPasswordEncoder(User user);
    public User getUser(Long id);
    public void deleteUser(Long id);
    public User findByEmail(String email);
    public User findByFirstName(String name);
}
