package com.example.demo.repository;

import com.example.demo.model.passwordResetToken;
import com.example.demo.model.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface passwordResetTokenRepository extends JpaRepository<passwordResetToken, Long> {
    
    passwordResetToken findByToken(String token);
    
    // Simplified method - Spring can handle this automatically
    List<passwordResetToken> findByDonor(Donor donor);
}