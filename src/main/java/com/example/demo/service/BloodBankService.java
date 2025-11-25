package com.example.demo.service;

import com.example.demo.model.BloodBank;
import com.example.demo.repository.BloodBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BloodBankService {

    @Autowired
    private BloodBankRepository bloodBankRepository;
    
    // Save blood bank
    public BloodBank saveBloodBank(BloodBank bloodBank) {
        try {
            System.out.println("💾 Saving blood bank: " + bloodBank.getName());
            BloodBank saved = bloodBankRepository.save(bloodBank);
            System.out.println("✅ Blood bank saved with SNo: " + saved.getSNo());
            return saved;
        } catch (Exception e) {
            System.err.println("❌ Error saving blood bank: " + e.getMessage());
            throw e;
        }
    }
    
    // Get all blood banks
    public List<BloodBank> getAllBloodBanks() {
        return bloodBankRepository.findAll();
    }
    
    // Get blood bank by ID
    public Optional<BloodBank> getBloodBankById(Long sNo) {
        return bloodBankRepository.findById(sNo);
    }
    
    // Get blood bank by blood bank ID
    public Optional<BloodBank> getBloodBankByBloodBankId(Integer bloodBankId) {
        return bloodBankRepository.findByBloodBankId(bloodBankId);
    }
    
    // Authenticate blood bank
    public Optional<BloodBank> authenticateBloodBank(String username, String password) {
        try {
            Optional<BloodBank> bloodBankOpt = bloodBankRepository.findByUsername(username);
            if (bloodBankOpt.isPresent()) {
                BloodBank bloodBank = bloodBankOpt.get();
                if (password.equals(bloodBank.getPassword())) {
                    System.out.println("✅ Blood bank login successful: " + username);
                    return bloodBankOpt;
                } else {
                    System.out.println("❌ Blood bank login failed - wrong password: " + username);
                }
            } else {
                System.out.println("❌ Blood bank not found: " + username);
            }
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("❌ Error authenticating blood bank: " + e.getMessage());
            return Optional.empty();
        }
    }
    
    // Update blood bank
    public BloodBank updateBloodBank(BloodBank bloodBank) {
        if (bloodBank.getSNo() != null && bloodBankRepository.existsById(bloodBank.getSNo())) {
            return bloodBankRepository.save(bloodBank);
        }
        return null;
    }
    
    // Delete blood bank
    public boolean deleteBloodBank(Long sNo) {
        if (bloodBankRepository.existsById(sNo)) {
            bloodBankRepository.deleteById(sNo);
            return true;
        }
        return false;
    }
    
    // Search blood banks by location
    public List<BloodBank> searchByLocation(String location) {
        return bloodBankRepository.findByAddressContainingIgnoreCase(location);
    }
    
    // Search blood banks by blood type availability
    public List<BloodBank> searchByBloodAvailability(String bloodType) {
        return bloodBankRepository.findBloodBanksWithBloodAvailable(bloodType);
    }
    
    // Search blood banks by name
    public List<BloodBank> searchByName(String name) {
        return bloodBankRepository.findByNameContainingIgnoreCase(name);
    }
    
    // Get blood banks by category
    public List<BloodBank> getBloodBanksByCategory(String category) {
        return bloodBankRepository.findByCategory(category);
    }
    
    // Get nearby blood banks
    public List<BloodBank> getNearbyBloodBanks(Double maxDistance) {
        return bloodBankRepository.findByDistanceLessThanEqual(maxDistance);
    }
    
    // Update blood stock
    public boolean updateBloodStock(Long sNo, String bloodType, int units) {
        Optional<BloodBank> bloodBankOpt = bloodBankRepository.findById(sNo);
        if (bloodBankOpt.isPresent()) {
            BloodBank bloodBank = bloodBankOpt.get();
            bloodBank.addBlood(bloodType, units);
            bloodBankRepository.save(bloodBank);
            System.out.println("✅ Updated " + bloodType + " stock by " + units + " units for " + bloodBank.getName());
            return true;
        }
        return false;
    }
    
    // Check if username exists
    public boolean usernameExists(String username) {
        return bloodBankRepository.existsByUsername(username);
    }
    
    // Check if email exists
    public boolean emailExists(String email) {
        return bloodBankRepository.existsByEmail(email);
    }
    
    // Check if blood bank ID exists
    public boolean bloodBankIdExists(Integer bloodBankId) {
        return bloodBankRepository.existsByBloodBankId(bloodBankId);
    }
}