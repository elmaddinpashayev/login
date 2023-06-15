package com.example.springsecurity.controller;

import com.example.springsecurity.model.model.User;
import com.example.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody User user) {
        User existingUser = userService.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "User already exists"
            );
        }
        User savedUser = userService.save(user);
        // send confirmation email...
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/signin")
    public ResponseEntity<User> signin(@RequestBody User user) {
        User existingUser = userService.findByEmail(user.getEmail());
        if (existingUser == null || !bCryptPasswordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Invalid email or password"
            );
        }
        return ResponseEntity.ok(existingUser);
    }

    @GetMapping("/me")
    public ResponseEntity<String> currentUser() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = userService.findByEmail(auth.getName());
        return ResponseEntity.ok("user");
    }
}