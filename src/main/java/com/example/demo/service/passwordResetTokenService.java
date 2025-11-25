package com.example.demo.service;

import com.example.demo.model.passwordResetToken;
import com.example.demo.model.Donor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class passwordResetTokenService {
    
    // In-memory storage - no database needed
    private Map<String, passwordResetToken> tokenStore = new HashMap<>();
    
    public passwordResetToken createPasswordResetTokenForUser(Donor donor) {
        // Remove existing tokens for this donor
        tokenStore.values().removeIf(token -> 
            token.getDonor().getId().equals(donor.getId()));
        
        // Create new token
        String token = UUID.randomUUID().toString();
        passwordResetToken resetToken = new passwordResetToken(token, donor);
        tokenStore.put(token, resetToken);
        return resetToken;
    }
    
    public passwordResetToken findByToken(String token) {
        return tokenStore.get(token);
    }
    
    public void delete(passwordResetToken token) {
        tokenStore.remove(token.getToken());
    }
}