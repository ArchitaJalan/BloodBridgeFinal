package com.example.demo.config;

import com.example.demo.model.Donor;
import com.example.demo.repository.DonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DonorRepository donorRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 DataInitializer started...");
        
        try {
            // Check if donors already exist to avoid duplicates
            long count = donorRepository.count();
            System.out.println("📊 Current donor count in database: " + count);
            
            if (count == 0) {
                System.out.println("💾 Initializing sample donor data...");

                // Create sample donor 1
                Donor donor1 = new Donor();
                donor1.setUsername("Archita_Jalan");
                donor1.setPassword("password123");
                donor1.setEmail("architajalan@gmail.com");
                donor1.setFullName("Archita Jalan");
                donor1.setBloodGroup("A+");
                donor1.setPhoneNumber("1234567890");
                donor1.setAddress("123 Main Street, New York, NY");
                System.out.println("✅ Created donor1: " + donor1.getUsername());

                // Create sample donor 2
                Donor donor2 = new Donor();
                donor2.setUsername("Mahaveer_Daga");
                donor2.setPassword("securepass");
                donor2.setEmail("mahaveer@gmail.com");
                donor2.setFullName("Mahaveer Daga");
                donor2.setBloodGroup("O-");
                donor2.setPhoneNumber("0987654321");
                donor2.setAddress("456 Oak Avenue, Los Angeles, CA");
                System.out.println("✅ Created donor2: " + donor2.getUsername());

                // Create sample donor 3
                Donor donor3 = new Donor();
                donor3.setUsername("Sakshi_Deshmukh");
                donor3.setPassword("mike2024");
                donor3.setEmail("sakshi@gmail.com");
                donor3.setFullName("Sakshi Deshmukh");
                donor3.setBloodGroup("B+");
                donor3.setPhoneNumber("5551234567");
                donor3.setAddress("789 Pine Road, Chicago, IL");
                System.out.println("✅ Created donor3: " + donor3.getUsername());

                // Create sample donor 4
                Donor donor4 = new Donor();
                donor4.setUsername("Rutuja_Dhavale");
                donor4.setPassword("sarah123");
                donor4.setEmail("rutuja@gmail.com");
                donor4.setFullName("Rutuja Dhavale");
                donor4.setBloodGroup("AB+");
                donor4.setPhoneNumber("4449876543");
                donor4.setAddress("321 Elm Street, Houston, TX");
                System.out.println("✅ Created donor4: " + donor4.getUsername());

                // Save all donors to database
                System.out.println("💾 Saving donors to database...");
                donorRepository.save(donor1);
                donorRepository.save(donor2);
                donorRepository.save(donor3);
                donorRepository.save(donor4);

                // Verify insertion
                long newCount = donorRepository.count();
                System.out.println("✅ Sample donor data inserted successfully!");
                System.out.println("📊 Total donors in database after insertion: " + newCount);
                
                // Print all donors for verification
                System.out.println("\n🧪 ALL DONORS IN DATABASE:");
                donorRepository.findAll().forEach(donor -> 
                    System.out.println("👤 " + donor.getUsername() + " | " + donor.getEmail())
                );
                
            } else {
                System.out.println("✅ Database already contains " + count + " donor(s)");
                System.out.println("\n📋 EXISTING DONORS:");
                donorRepository.findAll().forEach(donor -> 
                    System.out.println("👤 " + donor.getUsername() + " | " + donor.getEmail())
                );
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error in DataInitializer: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("🏁 DataInitializer completed.");
    }
}