package com.example.demo.controller;

import com.example.demo.model.Donor;
import com.example.demo.service.DonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/auth")  // Add base path for all methods in this controller
public class LoginController {

    @Autowired
    private DonorService donorService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(
            @RequestParam String username,
            @RequestParam String password,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            Optional<Donor> donorOptional = donorService.authenticateDonor(username, password);
            
            if (donorOptional.isPresent()) {
                Donor donor = donorOptional.get();
                session.setAttribute("loggedInDonor", donor);
                session.setAttribute("donorName", donor.getFullName());
                
                redirectAttributes.addFlashAttribute("success", 
                    "Welcome back, " + donor.getFullName() + "!");
                return "redirect:/index";
            } else {
                model.addAttribute("error", "Invalid username or password!");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Login error: " + e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("success", "You have been logged out successfully.");
        return "redirect:/auth/login";  // Updated redirect
    }
}