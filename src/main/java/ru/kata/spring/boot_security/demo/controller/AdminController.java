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

@Controller
@RequestMapping("/admin")
public class AdminController {

	private final UserService userService;
	private final RoleRepository roleRepository;

	@Autowired
	public AdminController(UserService userService, RoleRepository roleRepository) {
		this.userService = userService;
		this.roleRepository = roleRepository;
	}

	@GetMapping
	public String printUsers(Model model, Principal principal) {
		model.addAttribute("user", userService.findByFirstName(principal.getName()));
		model.addAttribute("users", userService.allUsers());
		model.addAttribute("roles", roleRepository.findAll());
		return "admin";
	}

	@PostMapping("/users")
	public String saveUser(User user, String rawPassword) {
		userService.setPasswordEncoder(user, rawPassword);
		userService.addOrUpdateUser(user, user.getId());
		return "redirect:/admin";
	}

	@PutMapping("/users/{id}")
	public String editUser(User user, @PathVariable("id") long id, String rawPassword) {
		userService.setPasswordEncoder(user, rawPassword);
		userService.addOrUpdateUser(user, id);
		return "redirect:/admin";
	}

	@DeleteMapping("/users/{id}")
	public String deleteUser(@PathVariable("id") long id) {
		userService.deleteUser(id);
		return "redirect:/admin";
	}
}