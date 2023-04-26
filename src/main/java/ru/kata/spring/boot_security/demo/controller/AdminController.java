package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/admin/api")
public class AdminController {

	private final UserService userService;
	private final RoleRepository roleRepository;

	@Autowired
	public AdminController(UserService userService, RoleRepository roleRepository) {
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

//	@PostMapping("/users")
//	public String saveUser(User user, String password) {
//		userService.setPasswordEncoder(user, password);
//		userService.addUser(user);
//		return "redirect:/admin";
//	}
//
//	@PatchMapping("/users/{id}")
//	public String editUser(User user, String password) {
//		userService.setPasswordEncoder(user, password);
//		userService.updateUser(user);
//		return "redirect:/admin";
//	}
//
//	@DeleteMapping("/users/{id}")
//	public String deleteUser(@PathVariable("id") long id) {
//		userService.deleteUser(id);
//		return "redirect:/admin";
//	}
}