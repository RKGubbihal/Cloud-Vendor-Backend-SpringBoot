package com.example.cloudvendor.service.impl;

import com.example.cloudvendor.entity.CloudVendor;
import com.example.cloudvendor.repository.CloudVendorRepository;
import com.example.cloudvendor.service.CloudVendorService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CloudVendorServiceImpl implements CloudVendorService {
    
    private static final Logger logger = LoggerFactory.getLogger(CloudVendorServiceImpl.class);
    private final CloudVendorRepository cloudVendorRepository;
    
    @Override
    public CloudVendor createVendor(CloudVendor cloudVendor) {
        logger.debug("Creating cloud vendor with ID: {}", cloudVendor.getVendorId());
        // Check if vendor with same ID already exists
        if (cloudVendorRepository.existsById(cloudVendor.getVendorId())) {
            logger.warn("Cloud Vendor with ID {} already exists", cloudVendor.getVendorId());
            throw new RuntimeException("Cloud Vendor with ID " + cloudVendor.getVendorId() + " already exists");
        }
        CloudVendor savedVendor = cloudVendorRepository.save(cloudVendor);
        logger.info("Cloud vendor created successfully with ID: {}", savedVendor.getVendorId());
        return savedVendor;
    }
    
    @Override
    public CloudVendor updateVendor(CloudVendor cloudVendor) {
        logger.debug("Updating cloud vendor with ID: {}", cloudVendor.getVendorId());
        // Check if vendor exists before updating
        if (!cloudVendorRepository.existsById(cloudVendor.getVendorId())) {
            logger.warn("Cloud Vendor with ID {} not found for update", cloudVendor.getVendorId());
            throw new RuntimeException("Cloud Vendor with ID " + cloudVendor.getVendorId() + " not found");
        }
        CloudVendor updatedVendor = cloudVendorRepository.save(cloudVendor);
        logger.info("Cloud vendor updated successfully with ID: {}", updatedVendor.getVendorId());
        return updatedVendor;
    }
    
    @Override
    public String deleteVendor(String vendorId) {
        logger.debug("Deleting cloud vendor with ID: {}", vendorId);
        // Check if vendor exists before deleting
        if (!cloudVendorRepository.existsById(vendorId)) {
            logger.warn("Cloud Vendor with ID {} not found for deletion", vendorId);
            throw new RuntimeException("Cloud Vendor with ID " + vendorId + " not found");
        }
        cloudVendorRepository.deleteById(vendorId);
        logger.info("Cloud vendor deleted successfully with ID: {}", vendorId);
        return "Cloud Vendor with ID " + vendorId + " deleted successfully";
    }
    
    @Override
    public CloudVendor getVendorById(String vendorId) {
        logger.debug("Retrieving cloud vendor with ID: {}", vendorId);
        CloudVendor vendor = cloudVendorRepository.findById(vendorId)
                .orElseThrow(() -> {
                    logger.warn("Cloud Vendor with ID {} not found", vendorId);
                    return new RuntimeException("Cloud Vendor with ID " + vendorId + " not found");
                });
        logger.info("Cloud vendor retrieved successfully with ID: {}", vendorId);
        return vendor;
    }
    
    @Override
    public List<CloudVendor> getAllVendors() {
        logger.debug("Retrieving all cloud vendors");
        List<CloudVendor> vendors = cloudVendorRepository.findAll();
        if (vendors.isEmpty()) {
            logger.warn("No cloud vendors found in the database");
            throw new RuntimeException("No Cloud Vendors found");
        }
        logger.info("Retrieved {} cloud vendor(s) successfully", vendors.size());
        return vendors;
    }
}
