package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "blood_bank")
public class BloodBank {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SNo")
    private Long sNo;
    
    @Column(name = "Name", nullable = false)
    private String name;
    
    @Column(name = "Address", nullable = false)
    private String address;
    
    @Column(name = "Phone", nullable = false)
    private String phone;
    
    @Column(name = "Email")
    private String email;
    
    @Column(name = "Category")
    private String category; // Govt, Private, NGO, RedCross
    
    @Column(name = "Distance")
    private Double distance;
    
    @Column(name = "Type")
    private String type;
    
    @Column(name = "id", nullable = false, unique = true)
    private Integer bloodBankId;
    
    @Column(name = "username", unique = true)
    private String username;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    // Blood Inventory Fields
    private int aPositive;
    private int aNegative;
    private int bPositive;
    private int bNegative;
    private int abPositive;
    private int abNegative;
    private int oPositive;
    private int oNegative;
    
    // Default Constructor
    public BloodBank() {}
    
    // Parameterized Constructor
    public BloodBank(String name, String address, String phone, String email, 
                    String category, Double distance, String type, 
                    Integer bloodBankId, String username, String password) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.category = category;
        this.distance = distance;
        this.type = type;
        this.bloodBankId = bloodBankId;
        this.username = username;
        this.password = password;
    }
    
    // Getters and Setters
    public Long getSNo() { return sNo; }
    public void setSNo(Long sNo) { this.sNo = sNo; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public Double getDistance() { return distance; }
    public void setDistance(Double distance) { this.distance = distance; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Integer getBloodBankId() { return bloodBankId; }
    public void setBloodBankId(Integer bloodBankId) { this.bloodBankId = bloodBankId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    // Blood Inventory Getters and Setters
    public int getAPositive() { return aPositive; }
    public void setAPositive(int aPositive) { this.aPositive = aPositive; }
    
    public int getANegative() { return aNegative; }
    public void setANegative(int aNegative) { this.aNegative = aNegative; }
    
    public int getBPositive() { return bPositive; }
    public void setBPositive(int bPositive) { this.bPositive = bPositive; }
    
    public int getBNegative() { return bNegative; }
    public void setBNegative(int bNegative) { this.bNegative = bNegative; }
    
    public int getAbPositive() { return abPositive; }
    public void setAbPositive(int abPositive) { this.abPositive = abPositive; }
    
    public int getAbNegative() { return abNegative; }
    public void setAbNegative(int abNegative) { this.abNegative = abNegative; }
    
    public int getOPositive() { return oPositive; }
    public void setOPositive(int oPositive) { this.oPositive = oPositive; }
    
    public int getONegative() { return oNegative; }
    public void setONegative(int oNegative) { this.oNegative = oNegative; }
    
    // Utility method to add blood units
    public void addBlood(String bloodType, int units) {
        switch (bloodType.toUpperCase()) {
            case "A+": this.aPositive += units; break;
            case "A-": this.aNegative += units; break;
            case "B+": this.bPositive += units; break;
            case "B-": this.bNegative += units; break;
            case "AB+": this.abPositive += units; break;
            case "AB-": this.abNegative += units; break;
            case "O+": this.oPositive += units; break;
            case "O-": this.oNegative += units; break;
        }
    }
    
    // Utility method to get blood count by type
    public int getBloodCount(String bloodType) {
        switch (bloodType.toUpperCase()) {
            case "A+": return aPositive;
            case "A-": return aNegative;
            case "B+": return bPositive;
            case "B-": return bNegative;
            case "AB+": return abPositive;
            case "AB-": return abNegative;
            case "O+": return oPositive;
            case "O-": return oNegative;
            default: return 0;
        }
    }
    
    @Override
    public String toString() {
        return "BloodBank{" +
                "sNo=" + sNo +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", category='" + category + '\'' +
                ", distance=" + distance +
                ", type='" + type + '\'' +
                ", bloodBankId=" + bloodBankId +
                ", username='" + username + '\'' +
                '}';
    }
}
