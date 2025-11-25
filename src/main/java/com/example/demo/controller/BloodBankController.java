//package com.example.demo.controller;

//import com.example.demo.model.BloodBank;
//import com.example.demo.service.BloodBankService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import jakarta.servlet.http.HttpSession;
//import jakarta.validation.Valid;
//import java.util.Optional;
//
//@Controller
//@RequestMapping("/auth")
//public class BloodBankAuthController {
//
//    @Autowired
//    private BloodBankService bloodBankService;
//
//    // ========== BLOOD BANK AUTHENTICATION ==========
//
//    @GetMapping("/bloodBankLogin")
//    public String showBloodBankLoginPage(Model model) {
//        model.addAttribute("loginError", false);
//        return "blood_bank_login"; // This should match your HTML file name
//    }
//
//    @PostMapping("/bloodBankLogin")
//    public String loginBloodBank(@RequestParam String username, 
//                               @RequestParam String password,
//                               HttpSession session,
//                               Model model,
//                               RedirectAttributes redirectAttributes) {
//        try {
//            System.out.println("=== BLOOD BANK LOGIN ATTEMPT ===");
//            System.out.println("Username: " + username);
//            
//            Optional<BloodBank> bloodBankOpt = bloodBankService.findByUsername(username);
//            
//            if (bloodBankOpt.isPresent()) {
//                BloodBank bloodBank = bloodBankOpt.get();
//                System.out.println("Found blood bank: " + bloodBank.getName());
//                
//                // For now, using plain text password matching
//                // In production, use PasswordEncoder
//                if (bloodBank.getPassword().equals(password)) {
//                    System.out.println("✅ Password matched - Login successful");
//                    
//                    // Set session attributes
//                    session.setAttribute("loggedInBloodBank", bloodBank);
//                    session.setAttribute("bloodBankName", bloodBank.getName());
//                    session.setAttribute("userType", "blood_bank");
//                    
//                    redirectAttributes.addFlashAttribute("success", 
//                        "Welcome back, " + bloodBank.getName() + "!");
//                    
//                    return "redirect:/blood-bank/dashboard"; // Redirect to blood bank dashboard
//                } else {
//                    System.out.println("❌ Password mismatch");
//                }
//            } else {
//                System.out.println("❌ Blood bank not found with username: " + username);
//            }
//            
//            model.addAttribute("loginError", true);
//            model.addAttribute("error", "Invalid username or password");
//            model.addAttribute("username", username); // Return username for convenience
//            return "blood_bank_login";
//            
//        } catch (Exception e) {
//            System.err.println("❌ BLOOD BANK LOGIN ERROR: " + e.getMessage());
//            e.printStackTrace();
//            
//            model.addAttribute("loginError", true);
//            model.addAttribute("error", "Login error: " + e.getMessage());
//            return "blood_bank_login";
//        }
//    }
//
//    @GetMapping("/bloodBankLogout")
//    public String logoutBloodBank(HttpSession session, RedirectAttributes redirectAttributes) {
//        session.removeAttribute("loggedInBloodBank");
//        session.removeAttribute("bloodBankName");
//        session.removeAttribute("userType");
//        redirectAttributes.addFlashAttribute("success", "Blood Bank logged out successfully.");
//        return "redirect:/index";
//    }
//
//    // ========== BLOOD BANK REGISTRATION ==========
//
//    @GetMapping("/bloodBankRegister")
//    public String showBloodBankRegistrationPage(Model model) {
//        System.out.println("✅ Loading blood bank registration form...");
//        model.addAttribute("bloodBank", new BloodBank());
//        return "blood_bank_register"; // Create this HTML template
//    }
//
//    @PostMapping("/bloodBankRegister")
//    public String registerBloodBank(@Valid @ModelAttribute("bloodBank") BloodBank bloodBank, 
//                                   BindingResult bindingResult,
//                                   Model model, 
//                                   HttpSession session,
//                                   RedirectAttributes redirectAttributes) {
//        try {
//            System.out.println("=== BLOOD BANK REGISTRATION PROCESS STARTED ===");
//            System.out.println("📝 Blood Bank Registration Data:");
//            System.out.println("Name: " + bloodBank.getName());
//            System.out.println("Username: " + bloodBank.getUsername());
//            System.out.println("Email: " + bloodBank.getEmail());
//            System.out.println("Phone: " + bloodBank.getPhone());
//            System.out.println("Category: " + bloodBank.getCategory());
//            System.out.println("Bank ID: " + bloodBank.getBankId());
//            
//            // Check for validation errors
//            if (bindingResult.hasErrors()) {
//                System.out.println("❌ Validation errors found:");
//                bindingResult.getFieldErrors().forEach(error -> 
//                    System.out.println("  - " + error.getField() + ": " + error.getDefaultMessage()));
//                model.addAttribute("error", "Please fix the validation errors above.");
//                return "blood_bank_register";
//            }
//            
//            // Check if username already exists
//            System.out.println("🔍 Checking if username exists: " + bloodBank.getUsername());
//            if (bloodBankService.usernameExists(bloodBank.getUsername())) {
//                System.out.println("❌ Username already exists: " + bloodBank.getUsername());
//                model.addAttribute("error", "Username already exists. Please choose a different username.");
//                return "blood_bank_register";
//            }
//            System.out.println("✅ Username is available");
//            
//            // Check if email already exists
//            if (bloodBank.getEmail() != null && !bloodBank.getEmail().isEmpty()) {
//                System.out.println("🔍 Checking if email exists: " + bloodBank.getEmail());
//                if (bloodBankService.emailExists(bloodBank.getEmail())) {
//                    System.out.println("❌ Email already exists: " + bloodBank.getEmail());
//                    model.addAttribute("error", "Email already registered. Please use a different email.");
//                    return "blood_bank_register";
//                }
//                System.out.println("✅ Email is available");
//            }
//            
//            // Check if bank ID already exists
//            System.out.println("🔍 Checking if bank ID exists: " + bloodBank.getBankId());
//            Optional<BloodBank> existingBank = bloodBankService.findByBankId(bloodBank.getBankId());
//            if (existingBank.isPresent()) {
//                System.out.println("❌ Bank ID already exists: " + bloodBank.getBankId());
//                model.addAttribute("error", "Bank ID already registered. Please use a different ID.");
//                return "blood_bank_register";
//            }
//            System.out.println("✅ Bank ID is available");
//            
//            // Save blood bank to database
//            System.out.println("💾 Attempting to save blood bank to database...");
//            BloodBank savedBloodBank = bloodBankService.registerBloodBank(bloodBank);
//            
//            if (savedBloodBank != null && savedBloodBank.getSNo() != null) {
//                System.out.println("✅ Blood Bank registered successfully with ID: " + savedBloodBank.getSNo());
//                
//                // Auto-login after registration
//                session.setAttribute("loggedInBloodBank", savedBloodBank);
//                session.setAttribute("bloodBankName", savedBloodBank.getName());
//                session.setAttribute("userType", "blood_bank");
//                
//                redirectAttributes.addFlashAttribute("success", 
//                    "🎉 Welcome to Blood Bridge, " + savedBloodBank.getName() + "! Your blood bank has been registered successfully.");
//                
//                System.out.println("✅ Registration completed successfully, redirecting to dashboard");
//                return "redirect:/blood-bank/dashboard";
//            } else {
//                System.out.println("❌ Failed to save blood bank");
//                model.addAttribute("error", "Registration failed. Please try again.");
//                return "blood_bank_register";
//            }
//            
//        } catch (Exception e) {
//            System.err.println("❌ BLOOD BANK REGISTRATION ERROR: " + e.getMessage());
//            e.printStackTrace();
//            
//            model.addAttribute("error", "Registration error: " + e.getMessage());
//            return "blood_bank_register";
//        }
//    }
//
//    // ========== BLOOD BANK DASHBOARD ==========
//
//    @GetMapping("/blood-bank/dashboard")
//    public String showBloodBankDashboard(HttpSession session, Model model) {
//        BloodBank bloodBank = (BloodBank) session.getAttribute("loggedInBloodBank");
//        
//        if (bloodBank == null) {
//            return "redirect:/auth/bloodBankLogin";
//        }
//        
//        model.addAttribute("bloodBank", bloodBank);
//        model.addAttribute("isLoggedIn", true);
//        model.addAttribute("userType", "blood_bank");
//        
//        return "blood_bank_dashboard"; // Create this HTML template
//    }
//}