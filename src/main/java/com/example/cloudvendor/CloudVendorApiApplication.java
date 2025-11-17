package com.example.cloudvendor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CloudVendorApiApplication {

    private static final Logger logger = LoggerFactory.getLogger(CloudVendorApiApplication.class);

    public static void main(String[] args) {
        logger.info("Starting Cloud Vendor API Application...");
        SpringApplication.run(CloudVendorApiApplication.class, args);
        logger.info("=================================================");
        logger.info("🚀 Cloud Vendor API Application Started Successfully!");
        logger.info("📊 Server running on: http://localhost:8080");
        logger.info("🔗 API Base URL: http://localhost:8080/cloudvendor");
        logger.info("💾 Database: MySQL (cloudvendor_db)");
        logger.info("=================================================");
    }
}
