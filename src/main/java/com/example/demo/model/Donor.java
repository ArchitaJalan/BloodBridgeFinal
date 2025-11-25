package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "donors")
public class Donor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Boolean healthy;
    private String medicalHistory;
    
    private String gender;
    
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;
    
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;
    
    @NotBlank(message = "Blood group is required")
    private String bloodGroup;
    
    @Size(max = 15, message = "Phone number is too long")
    private String phoneNumber;
    
    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;
    
    @NotNull(message = "Date of birth is required")
    private LocalDate dob;
    
    // Default Constructor
    public Donor() {}
    
    // Getters and Setters
    
    public Boolean getHealthy() {
        return healthy;
    }
    
    public void setHealthy(Boolean healthy) {
        this.healthy = healthy;
    }
    
    public String getMedicalHistory() {
        return medicalHistory;
    }
    
    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    
    // Utility method to calculate age
    @Transient
    public int getAge() {
        if (dob == null) {
            return 0;
        }
        return Period.between(dob, LocalDate.now()).getYears();
    }
    
    @Override
    public String toString() {
        return "Donor{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                '}';
    }
}