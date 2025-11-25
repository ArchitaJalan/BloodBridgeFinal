package com.example.demo.controller;

import com.example.demo.model.Donor;
import com.example.demo.model.passwordResetToken;
import com.example.demo.service.DonorService;
import com.example.demo.service.passwordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private DonorService donorService;

    @Autowired
    private passwordResetTokenService passwordResetTokenService;

    @Autowired
    private JavaMailSender mailSender; // Add this

    // Enhanced EmailService
    @org.springframework.stereotype.Service
    class EmailService {
        
        @Value("${app.base-url:http://localhost:8082}")
        private String baseUrl;
        
        public void sendPasswordResetEmail(String toEmail, String resetLink, String userName) {
            // For now, keep the console output for development
            System.out.println("=== PASSWORD RESET EMAIL ===");
            System.out.println("To: " + toEmail);
            System.out.println("Subject: Password Reset Request - BloodBridge");
            System.out.println("Message: ");
            System.out.println("Hello " + userName + ",");
            System.out.println();
            System.out.println("You requested to reset your password for your BloodBridge account.");
            System.out.println("Click the link below to reset your password:");
            System.out.println();
            System.out.println(resetLink);
            System.out.println();
            System.out.println("This link will expire in 24 hours.");
            System.out.println("If you didn't request this, please ignore this email.");
            System.out.println();
            System.out.println("Thank you,");
            System.out.println("BloodBridge Team");
            System.out.println("============================");
            
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(toEmail);
                message.setSubject("Password Reset Request - BloodBridge");
                message.setText(createEmailContent(userName, resetLink));
                mailSender.send(message);
                System.out.println("✅ Email sent successfully to: " + toEmail);
            } catch (Exception e) {
                System.err.println("❌ Failed to send email: " + e.getMessage());
                // Don't throw exception - just log it so user can still see the link in console
            }
            
        }
        
        private String createEmailContent(String userName, String resetLink) {
            return String.format(
                "Hello %s,\n\n" +
                "You requested to reset your password for your BloodBridge account.\n\n" +
                "Click the link below to reset your password:\n" +
                "%s\n\n" +
                "This link will expire in 24 hours.\n\n" +
                "If you didn't request this, please ignore this email.\n\n" +
                "Thank you,\n" +
                "BloodBridge Team",
                userName, resetLink
            );
        }
    }
    
    private EmailService emailService = new EmailService();

    // ===== EXISTING LOGIN MAPPINGS =====
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
                    "🎉 Welcome back, " + donor.getFullName() + "! Thank you for being a life-saving donor.");
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

    // ===== FORGOT PASSWORD MAPPINGS =====
    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String processForgotPassword(
            @RequestParam String email,
            Model model,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        
        try {
            Optional<Donor> donorOptional = donorService.findByEmail(email);
            
            if (donorOptional.isPresent()) {
                Donor donor = donorOptional.get();
                
                // Create reset token
                passwordResetToken resetToken = passwordResetTokenService.createPasswordResetTokenForUser(donor);
                
                // Generate reset link
                String resetLink = generateResetLink(request, resetToken.getToken());
                
                // Send email (simulated for now)
                emailService.sendPasswordResetEmail(donor.getEmail(), resetLink, donor.getFullName());
                
                // For development - show the link
                System.out.println("=== PASSWORD RESET LINK ===");
                System.out.println("For: " + donor.getEmail());
                System.out.println("Link: " + resetLink);
                System.out.println("Token: " + resetToken.getToken());
                System.out.println("============================");
                
                redirectAttributes.addFlashAttribute("success", 
                    "Password reset link has been sent to your email! Check your console for the link.");
                return "redirect:/auth/forgot-password";
                
            } else {
                model.addAttribute("error", "No account found with this email address.");
                return "forgotPassword";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error sending reset email. Please try again.");
            e.printStackTrace();
            return "forgotPassword";
        }
    }

    // ===== RESET PASSWORD MAPPINGS =====
    @GetMapping("/resetPassword")
    public String showResetPasswordPage(@RequestParam("token") String token, Model model) {
        try {
            passwordResetToken resetToken = passwordResetTokenService.findByToken(token);
            
            if (resetToken == null) {
                model.addAttribute("error", "Invalid reset token. Please request a new password reset.");
                return "resetPassword";
            }
            
            if (resetToken.isExpired()) {
                model.addAttribute("error", "Reset token has expired. Please request a new password reset.");
                return "resetPassword";
            }
            
            model.addAttribute("token", token);
            model.addAttribute("email", resetToken.getDonor().getEmail());
            return "resetPassword";
            
        } catch (Exception e) {
            model.addAttribute("error", "Error processing reset token. Please try again.");
            return "resetPassword";
        }
    }

    @PostMapping("/resetPassword")
    public String processResetPassword(
            @RequestParam String token,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("🔄 PROCESSING PASSWORD RESET...");
            
            // Validate passwords
            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("error", "Passwords do not match.");
                model.addAttribute("token", token);
                return "resetPassword";
            }

            if (newPassword.length() < 6) {
                model.addAttribute("error", "Password must be at least 6 characters long.");
                model.addAttribute("token", token);
                return "resetPassword";
            }

            // Find token
            passwordResetToken resetToken = passwordResetTokenService.findByToken(token);
            if (resetToken == null) {
                model.addAttribute("error", "Invalid reset token.");
                return "resetPassword";
            }
            
            if (resetToken.isExpired()) {
                model.addAttribute("error", "Reset token has expired.");
                return "resetPassword";
            }

            // Get donor from token
            Donor donor = resetToken.getDonor();
            System.out.println("🔑 Resetting password for: " + donor.getEmail());
            
            // UPDATE PASSWORD
            boolean updateSuccess = donorService.updatePassword(donor, newPassword);
            
            if (updateSuccess) {
                // Delete used token
                passwordResetTokenService.delete(resetToken);
                
                redirectAttributes.addFlashAttribute("success", 
                    "Password has been reset successfully! You can now login with your new password.");
                return "redirect:/auth/login";
            } else {
                model.addAttribute("error", "Failed to update password. Please try again.");
                model.addAttribute("token", token);
                return "resetPassword";
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error in reset password: " + e.getMessage());
            model.addAttribute("error", "Error resetting password. Please try again.");
            model.addAttribute("token", token);
            return "resetPassword";
        }
    }
    
    // ===== EXISTING MAPPINGS =====
    @GetMapping("/my-account")
    public String showMyAccount(HttpSession session, Model model) {
        Donor loggedInDonor = (Donor) session.getAttribute("loggedInDonor");
        
        if (loggedInDonor == null) {
            return "redirect:/auth/login";
        }
        
        Optional<Donor> currentDonor = donorService.getDonorById(loggedInDonor.getId());
        
        if (currentDonor.isPresent()) {
            model.addAttribute("donor", currentDonor.get());
            session.setAttribute("loggedInDonor", currentDonor.get());
            session.setAttribute("donorName", currentDonor.get().getFullName());
        } else {
            model.addAttribute("error", "User not found");
            return "redirect:/auth/login";
        }
        
        return "myAccount"; 
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("success", "You have been logged out successfully.");
        return "redirect:/auth/login";
    }

    // ===== HELPER METHODS =====
    private String generateResetLink(HttpServletRequest request, String token) {
        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "") + request.getContextPath();
        return baseUrl + "/auth/resetPassword?token=" + token;
    }
}