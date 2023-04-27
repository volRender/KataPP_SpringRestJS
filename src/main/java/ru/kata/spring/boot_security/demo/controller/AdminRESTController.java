package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/admin")
public class AdminRESTController {

	private final UserService userService;

	@Autowired
	public AdminRESTController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<Collection<User>> printUsers() {
		return new ResponseEntity<>(userService.allUsers(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
		return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<User> saveUser(@RequestBody User user) {
		userService.setPasswordEncoder(user);
		userService.addUser(user);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<User> editUser(@RequestBody User user) {
		userService.setPasswordEncoder(user);
		userService.updateUser(user);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable("id") long id) {
		userService.deleteUser(id);
		return new ResponseEntity<>("User was deleted!", HttpStatus.OK);
	}
}