package ru.kata.spring.boot_security.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin/api")
public class UsersRestController {

	private final UserService userService;
	private final RoleRepository roleRepository;

	@Autowired
	public UsersRestController(UserService userService, RoleRepository roleRepository) {
		this.userService = userService;
		this.roleRepository = roleRepository;
	}

	@GetMapping("/users")
	public List<User> printUsers() {
		return userService.allUsers();
	}

	@GetMapping("/users/{id}")
	public User getUser(@PathVariable Long id) {
		return userService.getUser(id);
	}

	@PostMapping("/users")
	public User saveUser(@RequestBody User user) {
		userService.setPasswordEncoder(user);
		userService.addOrUpdateUser(user);
		return user;
	}

	@PutMapping("/users")
	public User editUser(@RequestBody User user) {
		userService.setPasswordEncoder(user);
		userService.addOrUpdateUser(user);
		return user;
	}

	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
	}
}