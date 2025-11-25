package com.example.demo.model;

import jakarta.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "password_reset_tokens")
public class passwordResetToken {
    
    private static final int EXPIRATION = 60 * 24; // 24 hours in minutes
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String token;
    
    @OneToOne(targetEntity = Donor.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "donor_id")
    private Donor donor;
    
    @Column(nullable = false)
    private Date expiryDate;
    
    public passwordResetToken() {
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }
    
    public passwordResetToken(String token, Donor donor) {
        this.token = token;
        this.donor = donor;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }
    
    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }
    
    // Check if token is expired
    public boolean isExpired() {
        return new Date().after(this.expiryDate);
    }
    
    // Generate random token
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public Donor getDonor() { return donor; }
    public void setDonor(Donor donor) { this.donor = donor; }
    
    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }
}