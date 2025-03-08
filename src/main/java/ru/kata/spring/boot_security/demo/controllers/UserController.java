package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {
    @GetMapping
    public String userPage(Model model, Principal principal) {
        String email = principal.getName();
        model.addAttribute("username", email);
        return "user";
    }
}