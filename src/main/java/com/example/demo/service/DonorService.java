package com.example.demo.service;

import com.example.demo.model.Donor;
import com.example.demo.repository.DonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class DonorService {

    @Autowired
    private DonorRepository donorRepository;

    // FIXED: Plain text password authentication
    public Optional<Donor> authenticateDonor(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        Optional<Donor> donorOptional = donorRepository.findByUsername(username.trim());
        if (donorOptional.isPresent()) {
            Donor donor = donorOptional.get();
            // FIXED: Simple plain text comparison
            if (password.trim().equals(donor.getPassword())) {
                System.out.println("✅ LOGIN SUCCESS - Plain text match for: " + username);
                return donorOptional;
            } else {
                System.out.println("❌ LOGIN FAILED - Password doesn't match for: " + username);
                System.out.println("Entered: '" + password + "'");
                System.out.println("Stored: '" + donor.getPassword() + "'");
            }
        } else {
            System.out.println("❌ User not found: " + username);
        }
        return Optional.empty();
    }

    // Find donor by username
    public Optional<Donor> findByUsername(String username) {
        return donorRepository.findByUsername(username);
    }

    // Check if email exists
    public boolean emailExists(String email) {
        try {
            return donorRepository.existsByEmail(email);
        } catch (Exception e) {
            System.err.println("Error checking email existence: " + e.getMessage());
            return false;
        }
    }

    // Check if username exists
    public boolean usernameExists(String username) {
        try {
            return donorRepository.existsByUsername(username);
        } catch (Exception e) {
            System.err.println("Error checking username existence: " + e.getMessage());
            return false;
        }
    }

    // Validate donor data
    public String validateDonorData(Donor donor) {
        System.out.println("🔍 Validating donor: " + donor.getFullName());
        
        if (donor.getDob() == null) {
            return "Date of birth is required";
        }
        
        if (donor.getDob() != null) {
            int age = Period.between(donor.getDob(), LocalDate.now()).getYears();
            System.out.println("📅 Calculated age: " + age);
            if (age < 18) {
                return "You must be at least 18 years old to register as a donor";
            }
            if (age > 65) {
                return "Donors must be under 65 years old";
            }
        }

        if (donor.getPhoneNumber() != null && !donor.getPhoneNumber().isEmpty()) {
            if (!donor.getPhoneNumber().matches("^[0-9]{10}$")) {
                return "Phone number must be exactly 10 digits";
            }
        }

        if (donor.getBloodGroup() == null || donor.getBloodGroup().isEmpty()) {
            return "Blood group is required";
        }
        
        if (!donor.getBloodGroup().matches("^(A|B|AB|O)[+-]$")) {
            return "Please provide a valid blood group (A+, A-, B+, B-, AB+, AB-, O+, O-)";
        }

        return null;
    }

    // FIXED: Save donor with PLAIN TEXT password
    public Donor saveDonor(Donor donor) {
        try {
            System.out.println("💾 Saving donor to database (PLAIN TEXT PASSWORD):");
            System.out.println("  - Name: " + donor.getFullName());
            System.out.println("  - Username: " + donor.getUsername());
            System.out.println("  - Email: " + donor.getEmail());
            System.out.println("  - Password: " + donor.getPassword() + " (saved as plain text)");
            System.out.println("  - Blood Group: " + donor.getBloodGroup());
            
            // ✅ NO ENCODING - save password as plain text
            Donor savedDonor = donorRepository.save(donor);
            System.out.println("✅ Donor saved with ID: " + savedDonor.getId());
            return savedDonor;
        } catch (Exception e) {
            System.err.println("❌ Error saving donor: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save donor: " + e.getMessage(), e);
        }
    }

    // Get all donors
    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }
    
    public Optional<Donor> findByEmail1(String email) {
        return donorRepository.findByEmail(email);
    }

    // FIXED: Update password with PLAIN TEXT
    public boolean updatePassword(Donor donor, String newPassword) {
        try {
            System.out.println("🔄 UPDATING PASSWORD (PLAIN TEXT)");
            System.out.println("Donor: " + donor.getEmail());
            System.out.println("New password: " + newPassword);
            
            // Get fresh donor from database
            Optional<Donor> freshDonorOpt = donorRepository.findById(donor.getId());
            if (!freshDonorOpt.isPresent()) {
                System.out.println("❌ Donor not found");
                return false;
            }
            
            Donor freshDonor = freshDonorOpt.get();
            System.out.println("Old password in DB: " + freshDonor.getPassword());
            
            // ✅ SET PASSWORD AS PLAIN TEXT - NO ENCODING
            freshDonor.setPassword(newPassword);
            
            // Save to database
            donorRepository.save(freshDonor);
            System.out.println("✅ Plain text password saved to database");
            
            // Verify
            Optional<Donor> verifiedDonor = donorRepository.findById(donor.getId());
            if (verifiedDonor.isPresent()) {
                String actualInDB = verifiedDonor.get().getPassword();
                System.out.println("✅ Verified - Password in DB now: " + actualInDB);
                
                // Check if it matches exactly
                if (newPassword.equals(actualInDB)) {
                    System.out.println("🎉 PLAIN TEXT PASSWORD UPDATE SUCCESSFUL!");
                    return true;
                }
            }
            
            return false;
            
        } catch (Exception e) {
            System.err.println("💥 Update password error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Find donor by ID
    public Optional<Donor> findById(Long id) {
        return donorRepository.findById(id);
    }

    // Update donor
    public Donor updateDonor(Donor donor) {
        if (donor.getId() != null && donorRepository.existsById(donor.getId())) {
            return donorRepository.save(donor);
        }
        return null;
    }
    
    public Optional<Donor> getDonorById(Long id) {
        return donorRepository.findById(id);
    }
   
    public Optional<Donor> findByEmail(String email) {
        return donorRepository.findByEmail(email);
    }
    
    // Delete donor
    public boolean deleteDonor(Long id) {
        if (donorRepository.existsById(id)) {
            donorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}