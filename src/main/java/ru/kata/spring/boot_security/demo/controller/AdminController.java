package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

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
	public String printUsers(Model model) {
		model.addAttribute("users", userService.allUsers());
		return "users";
	}

	@GetMapping("/new")
	public String newUser(Model model) {
		List<Role> roles = roleRepository.findAll();
		model.addAttribute("newUser", new User());
		model.addAttribute("roles", roles);
		return "newUser";
	}

	@PostMapping
	public String saveUser(@ModelAttribute("newUser") User user) {
		userService.setPasswordEncoder(user);
		userService.addOrUpdateUser(user, user.getId());
		return "redirect:/admin";
	}

	@GetMapping("/{id}/edit")
	public String getUser(@PathVariable("id") long id, Model model) {
		List<Role> roles = roleRepository.findAll();
		model.addAttribute("editedUser", userService.getUser(id));
		model.addAttribute("roles", roles);
		return "editUser";
	}

	@PutMapping("/{id}")
	public String editUser(@ModelAttribute("editedUser") User user, @PathVariable("id") long id) {
		userService.setPasswordEncoder(user);
		userService.addOrUpdateUser(user, id);
		return "redirect:/admin";
	}

	@DeleteMapping("/{id}")
	public String deleteUser(@PathVariable("id") long id) {
		userService.deleteUser(id);
		return "redirect:/admin";
	}
}