package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, CartRepository cartRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.of(userRepository.findById(id));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        Cart cart = new Cart();
        cartRepository.save(cart);
        user.setCart(cart);
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

}
