package com.example.cloudvendor.service.impl;

import com.example.cloudvendor.entity.CloudVendor;
import com.example.cloudvendor.repository.CloudVendorRepository;
import com.example.cloudvendor.service.CloudVendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CloudVendorServiceImpl implements CloudVendorService {
    
    private final CloudVendorRepository cloudVendorRepository;
    
    @Override
    public CloudVendor createVendor(CloudVendor cloudVendor) {
        // Check if vendor with same ID already exists
        if (cloudVendorRepository.existsById(cloudVendor.getVendorId())) {
            throw new RuntimeException("Cloud Vendor with ID " + cloudVendor.getVendorId() + " already exists");
        }
        return cloudVendorRepository.save(cloudVendor);
    }
    
    @Override
    public CloudVendor updateVendor(CloudVendor cloudVendor) {
        // Check if vendor exists before updating
        if (!cloudVendorRepository.existsById(cloudVendor.getVendorId())) {
            throw new RuntimeException("Cloud Vendor with ID " + cloudVendor.getVendorId() + " not found");
        }
        return cloudVendorRepository.save(cloudVendor);
    }
    
    @Override
    public String deleteVendor(String vendorId) {
        // Check if vendor exists before deleting
        if (!cloudVendorRepository.existsById(vendorId)) {
            throw new RuntimeException("Cloud Vendor with ID " + vendorId + " not found");
        }
        cloudVendorRepository.deleteById(vendorId);
        return "Cloud Vendor with ID " + vendorId + " deleted successfully";
    }
    
    @Override
    public CloudVendor getVendorById(String vendorId) {
        return cloudVendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Cloud Vendor with ID " + vendorId + " not found"));
    }
    
    @Override
    public List<CloudVendor> getAllVendors() {
        List<CloudVendor> vendors = cloudVendorRepository.findAll();
        if (vendors.isEmpty()) {
            throw new RuntimeException("No Cloud Vendors found");
        }
        return vendors;
    }
}
