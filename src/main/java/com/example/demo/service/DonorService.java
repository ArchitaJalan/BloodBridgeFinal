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

    // Authenticate donor
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
            if (donor.getPassword().equals(password.trim())) {
                return donorOptional;
            }
        }
        return Optional.empty();
    }

    // Find donor by username
    public Optional<Donor> findByUsername(String username) {
        return donorRepository.findByUsername(username);
    }

    // Check if email exists
    public boolean emailExists(String email) {
        return donorRepository.existsByEmail(email);
    }

    // Check if username exists
    public boolean usernameExists(String username) {
        return donorRepository.existsByUsername(username);
    }

    // Validate donor data
    public String validateDonorData(Donor donor) {
        if (donor.getDob() != null) {
            int age = Period.between(donor.getDob(), LocalDate.now()).getYears();
            if (age < 18) {
                return "You must be at least 18 years old to register as a donor";
            }
            if (age > 65) {
                return "Donors must be under 65 years old";
            }
        }

        if (donor.getPhoneNumber() != null && !donor.getPhoneNumber().matches("^[0-9]{10}$")) {
            return "Phone number must be exactly 10 digits";
        }

        if (donor.getBloodGroup() != null && !donor.getBloodGroup().matches("^(A|B|AB|O)[+-]$")) {
            return "Please provide a valid blood group (A+, A-, B+, B-, AB+, AB-, O+, O-)";
        }

        return null;
    }

    // Save donor
    public Donor saveDonor(Donor donor) {
        try {
            return donorRepository.save(donor);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save donor: " + e.getMessage());
        }
    }

    // Get all donors
    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
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
    
 // Add this method to your DonorService class
    public Donor findByEmail(String email) {
        // You'll need to add findByEmail method to your DonorRepository first
        Optional<Donor> donorOptional = donorRepository.findByEmail(email);
        return donorOptional.orElse(null);
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