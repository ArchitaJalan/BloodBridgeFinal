package com.example.demo.controller;
import com.example.demo.model.Donor;
import com.example.demo.repository.DonorRepository;
import com.example.demo.service.DonorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

    @Autowired
    private DonorService donorService;

    // Your existing methods...
    @GetMapping("/")
    public String root(HttpSession session, Model model) {
        return handleIndexPage(session, model);
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        return handleIndexPage(session, model);
    }

    @GetMapping("/index")
    public String index(HttpSession session, Model model) {
        return handleIndexPage(session, model);
    }

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
    

    @GetMapping("/test-template")
    public String testTemplate(Model model) {
        model.addAttribute("isLoggedIn", false);
        return "index"; // This will use the template
    }

    @GetMapping("/looking_for_blood")
    public String lookingForBlood() {
        return "looking_for_blood";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginError", false);
        return "login";
    }

//    @GetMapping("/logout")
//    public String logoutDonor(HttpSession session, RedirectAttributes redirectAttributes) {
//        session.removeAttribute("loggedInDonor");
//        session.invalidate();
//        redirectAttributes.addFlashAttribute("success", "You have been logged out successfully.");
//        return "redirect:/index";
//    }

    @GetMapping("/want_to_donate_blood")
    public String wantToDonateBlood() {
        return "want_to_donate_blood";
    }

    @GetMapping("/want_to_donate_login")
    public String donateLoginPage() {
        return "want_to_donate_login";
    }
    
    // ✅ REGISTRATION ENDPOINTS - WITH ENHANCED ERROR HANDLING
    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        System.out.println("✅ Loading registration form...");
        model.addAttribute("donor", new Donor());
        return "register";
    }
    
    @Autowired
    private DonorRepository donorRepository;
    
 // Add this to your PageController temporarily for testing
    @GetMapping("/test-db")
    @ResponseBody
    public String testDatabase() {
        try {
            long count = donorRepository.count();
            return "Database connection successful! Total donors: " + count;
        } catch (Exception e) {
            return "Database connection failed: " + e.getMessage();
        }
    }

    @PostMapping("/register")
    public String registerDonor(@Valid @ModelAttribute("donor") Donor donor, 
                               BindingResult bindingResult,
                               Model model, 
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        try {
            System.out.println("=== REGISTRATION PROCESS STARTED ===");
            System.out.println("📝 Registration Data Received:");
            System.out.println("Name: " + donor.getFullName());
            System.out.println("Username: " + donor.getUsername());
            System.out.println("Email: " + donor.getEmail());
            System.out.println("DOB: " + donor.getDob());
            System.out.println("Blood Group: " + donor.getBloodGroup());
            System.out.println("Password: " + (donor.getPassword() != null ? "[PROVIDED]" : "NULL"));
            
            // Check for validation errors
            if (bindingResult.hasErrors()) {
                System.out.println("❌ Validation errors found:");
                bindingResult.getFieldErrors().forEach(error -> 
                    System.out.println("  - " + error.getField() + ": " + error.getDefaultMessage()));
                model.addAttribute("error", "Please fix the validation errors above.");
                return "register";
            }
            
            // Check if username already exists
            System.out.println("🔍 Checking if username exists: " + donor.getUsername());
            if (donorService.usernameExists(donor.getUsername())) {
                System.out.println("❌ Username already exists: " + donor.getUsername());
                model.addAttribute("error", "Username already exists. Please choose a different username.");
                return "register";
            }
            System.out.println("✅ Username is available");
            
            // Check if email already exists
            System.out.println("🔍 Checking if email exists: " + donor.getEmail());
            if (donorService.emailExists(donor.getEmail())) {
                System.out.println("❌ Email already exists: " + donor.getEmail());
                model.addAttribute("error", "Email already registered. Please use a different email.");
                return "register";
            }
            System.out.println("✅ Email is available");
            
            // Additional validation using service
            System.out.println("🔍 Validating donor data...");
            String validationError = donorService.validateDonorData(donor);
            if (validationError != null) {
                System.out.println("❌ Validation error: " + validationError);
                model.addAttribute("error", validationError);
                return "register";
            }
            System.out.println("✅ Data validation passed");
            
            // Save donor to database
            System.out.println("💾 Attempting to save donor to database...");
            Donor savedDonor = donorService.saveDonor(donor);
            
            if (savedDonor != null && savedDonor.getId() != null) {
                System.out.println("✅ Donor registered successfully with ID: " + savedDonor.getId());
                
                // ✅ AUTO-LOGIN AFTER REGISTRATION
                session.setAttribute("loggedInDonor", savedDonor);
                session.setAttribute("donorName", savedDonor.getFullName());
                
                // ✅ REDIRECT TO INDEX WITH WELCOME MESSAGE
                redirectAttributes.addFlashAttribute("success", 
                    "🎉 Welcome to Blood Bridge, " + savedDonor.getFullName() + "! Thank you for registering as a blood donor.");
                
                System.out.println("✅ Registration completed successfully, redirecting to index");
                return "redirect:/index";
            } else {
                System.out.println("❌ Failed to save donor - savedDonor is null or has no ID");
                model.addAttribute("error", "Registration failed. Please try again.");
                return "register";
            }
            
        } catch (Exception e) {
            System.err.println("❌ REGISTRATION ERROR: " + e.getMessage());
            e.printStackTrace();
            System.err.println("Error class: " + e.getClass().getName());
            System.err.println("Stack trace:");
            e.printStackTrace();
            
            model.addAttribute("error", "Registration error: " + e.getMessage());
            return "register";
        }
    }
}