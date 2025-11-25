package com.example.demo.repository;

import com.example.demo.model.BloodBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BloodBankRepository extends JpaRepository<BloodBank, Long> {
    
    // Find by username
    Optional<BloodBank> findByUsername(String username);
    
    // Find by email
    Optional<BloodBank> findByEmail(String email);
    
    // Find by blood bank ID
    Optional<BloodBank> findByBloodBankId(Integer bloodBankId);
    
    // Check if username exists
    boolean existsByUsername(String username);
    
    // Check if email exists
    boolean existsByEmail(String email);
    
    // Check if blood bank ID exists
    boolean existsByBloodBankId(Integer bloodBankId);
    
    // Find blood banks by category
    List<BloodBank> findByCategory(String category);
    
    // Find blood banks within maximum distance
    List<BloodBank> findByDistanceLessThanEqual(Double maxDistance);
    
    // Search blood banks by name (partial match)
    List<BloodBank> findByNameContainingIgnoreCase(String name);
    
    // Find blood banks with specific blood type available
    @Query("SELECT bb FROM BloodBank bb WHERE " +
           "(:bloodType = 'A+' AND bb.aPositive > 0) OR " +
           "(:bloodType = 'A-' AND bb.aNegative > 0) OR " +
           "(:bloodType = 'B+' AND bb.bPositive > 0) OR " +
           "(:bloodType = 'B-' AND bb.bNegative > 0) OR " +
           "(:bloodType = 'AB+' AND bb.abPositive > 0) OR " +
           "(:bloodType = 'AB-' AND bb.abNegative > 0) OR " +
           "(:bloodType = 'O+' AND bb.oPositive > 0) OR " +
           "(:bloodType = 'O-' AND bb.oNegative > 0)")
    List<BloodBank> findBloodBanksWithBloodAvailable(@Param("bloodType") String bloodType);
    
    // Find blood banks by location (address contains)
    List<BloodBank> findByAddressContainingIgnoreCase(String location);
}
