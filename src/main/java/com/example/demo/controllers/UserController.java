package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.services.UserService;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "users/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        userService.registerNewUser(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        System.out.println("Login page requested");
        return "users/login";
    }

    @GetMapping("/profile")
    public String showProfilePage(Model model, Principal principal) {
        // Get the username of the logged-in user
        String username = principal.getName();

        // Fetch the user from the database using the username
        User user = userService.findUserByUsername(username);

        // Add user details to the model to be displayed on the profile page
        model.addAttribute("user", user);

        return "users/profile";
    }
}
