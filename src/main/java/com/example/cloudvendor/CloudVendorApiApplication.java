package com.example.cloudvendor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CloudVendorApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudVendorApiApplication.class, args);
        System.out.println("=================================================");
        System.out.println("🚀 Cloud Vendor API Application Started Successfully!");
        System.out.println("📊 Server running on: http://localhost:8080");
        System.out.println("🔗 API Base URL: http://localhost:8080/cloudvendor");
        System.out.println("💾 Database: MySQL (cloudvendor_db)");
        System.out.println("=================================================");
    }
}
