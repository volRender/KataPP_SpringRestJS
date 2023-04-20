package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public String printUsers(Model model) {
		model.addAttribute("users", userService.allUsers());
		return "users";
	}

	@GetMapping("/new")
	public String newUser(Model model) {
		model.addAttribute("newUser", new User());
		return "newUser";
	}

	@PostMapping
	public String saveUser(@ModelAttribute("newUser") User user) {
		userService.addOrUpdateUser(user);
		return "redirect:/users";
	}

	@GetMapping("/{id}/edit")
	public String getUser(@PathVariable("id") long id, Model model) {
		model.addAttribute("editedUser", userService.getUser(id));
		return "editUser";
	}

	@PatchMapping("/{id}")
	public String editUser(@ModelAttribute("editedUser") User user, @PathVariable("id") long id) {
		user.setId(id);
		userService.addOrUpdateUser(user);
		return "redirect:/users";
	}

	@DeleteMapping("/{id}")
	public String deleteUser(@PathVariable("id") long id) {
		userService.deleteUser(id);
		return "redirect:/users";
	}
}