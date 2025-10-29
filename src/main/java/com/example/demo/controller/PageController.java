package com.example.demo.controller;

import com.example.demo.model.Donor;
import com.example.demo.service.DonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Optional;

@Controller
public class PageController {

    @Autowired
    private DonorService donorService;

    // ✅ Root URL mapping with session check
    @GetMapping("/")
    public String root(HttpSession session, Model model) {
        return handleIndexPage(session, model);
    }

    // ✅ Home page with session check
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        return handleIndexPage(session, model);
    }

    // ✅ Index page with session check
    @GetMapping("/index")
    public String index(HttpSession session, Model model) {
        return handleIndexPage(session, model);
    }

    // Common method to handle index page with login check
    private String handleIndexPage(HttpSession session, Model model) {
        Donor loggedInDonor = (Donor) session.getAttribute("loggedInDonor");
        
        if (loggedInDonor != null) {
            model.addAttribute("donor", loggedInDonor);
            model.addAttribute("isLoggedIn", true);
        } else {
            model.addAttribute("isLoggedIn", false);
        }
        
        return "index";
    }

    @GetMapping("/looking_for_blood")
    public String lookingForBlood() {
        return "looking_for_blood";
    }
    
    // ✅ Show login page (GET request)
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginError", false);
        return "login";
    }

    // ✅ Handle login form submission - FIXED
    @PostMapping("/login")
    public String loginDonor(@RequestParam String username, 
                           @RequestParam String password,
                           HttpSession session,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            if (username == null || username.trim().isEmpty()) {
                model.addAttribute("error", "Username is required");
                model.addAttribute("loginError", true);
                return "login";
            }
            
            if (password == null || password.trim().isEmpty()) {
                model.addAttribute("error", "Password is required");
                model.addAttribute("loginError", true);
                return "login";
            }

            // FIXED: Properly handle Optional<Donor>
            Optional<Donor> donorOptional = donorService.authenticateDonor(username.trim(), password.trim());
            if (donorOptional.isPresent()) {
                Donor donor = donorOptional.get(); // Extract Donor from Optional
                session.setAttribute("loggedInDonor", donor);
                redirectAttributes.addFlashAttribute("success", "Login successful! Welcome back, " + donor.getFullName() + "!");
                return "redirect:/index";
            } else {
                model.addAttribute("error", "Invalid username or password");
                model.addAttribute("loginError", true);
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Login error: " + e.getMessage());
            model.addAttribute("loginError", true);
            return "login";
        }
    }

    // ✅ Logout endpoint
    @GetMapping("/logout")
    public String logoutDonor(HttpSession session, RedirectAttributes redirectAttributes) {
        session.removeAttribute("loggedInDonor");
        session.invalidate();
        redirectAttributes.addFlashAttribute("success", "You have been logged out successfully.");
        return "redirect:/index";
    }

    @GetMapping("/want_to_donate_blood")
    public String wantToDonateBlood() {
        return "want_to_donate_blood";
    }

    @GetMapping("/want_to_donate_login")
    public String donateLoginPage() {
        return "want_to_donate_login";
    }
    
    // ✅ Show registration page (GET request)
    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        System.out.println("✅ Loading registration form...");
        model.addAttribute("donor", new Donor());
        return "register";
    }

    // ✅ Handle registration form submission - FIXED
    @PostMapping("/register")
    public String registerDonor(@Valid @ModelAttribute Donor donor, 
                               BindingResult bindingResult,
                               Model model, 
                               RedirectAttributes redirectAttributes) {
        try {
            System.out.println("📝 Registration Data Received:");
            System.out.println("Name: " + donor.getFullName());
            System.out.println("Username: " + donor.getUsername());
            System.out.println("Email: " + donor.getEmail());
            
            if (bindingResult.hasErrors()) {
                System.out.println("❌ Validation errors found:");
                bindingResult.getFieldErrors().forEach(error -> 
                    System.out.println(error.getField() + ": " + error.getDefaultMessage()));
                return "register";
            }
            
            // Additional business logic validations
            String validationError = donorService.validateDonorData(donor);
            if (validationError != null) {
                model.addAttribute("error", validationError);
                return "register";
            }
            
            // FIXED: Use existing methods
            if (donorService.usernameExists(donor.getUsername())) {
                model.addAttribute("error", "Username already exists. Please choose a different username.");
                return "register";
            }
            
            // FIXED: Use existing methods
            if (donorService.emailExists(donor.getEmail())) {
                model.addAttribute("error", "Email already registered. Please use a different email.");
                return "register";
            }
            
            // Save donor to database
            Donor savedDonor = donorService.saveDonor(donor);
            
            if (savedDonor != null) {
                redirectAttributes.addFlashAttribute("success", 
                    "Registration successful! You can now login with your credentials.");
                return "redirect:/login";
            } else {
                model.addAttribute("error", "Registration failed. Please try again.");
                return "register";
            }
            
        } catch (Exception e) {
            model.addAttribute("error", "Registration error: " + e.getMessage());
            return "register";
        }
    }
}