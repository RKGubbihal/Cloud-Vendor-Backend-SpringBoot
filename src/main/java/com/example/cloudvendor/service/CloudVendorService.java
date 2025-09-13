package com.example.cloudvendor.service;

import com.example.cloudvendor.entity.CloudVendor;

import java.util.List;

public interface CloudVendorService {
    
    /**
     * Create a new cloud vendor
     * @param cloudVendor the cloud vendor to create
     * @return the created cloud vendor
     */
    CloudVendor createVendor(CloudVendor cloudVendor);
    
    /**
     * Update an existing cloud vendor
     * @param cloudVendor the cloud vendor with updated information
     * @return the updated cloud vendor
     */
    CloudVendor updateVendor(CloudVendor cloudVendor);
    
    /**
     * Delete a cloud vendor by ID
     * @param vendorId the ID of the vendor to delete
     * @return success message
     */
    String deleteVendor(String vendorId);
    
    /**
     * Get a cloud vendor by ID
     * @param vendorId the ID of the vendor to retrieve
     * @return the cloud vendor
     */
    CloudVendor getVendorById(String vendorId);
    
    /**
     * Get all cloud vendors
     * @return list of all cloud vendors
     */
    List<CloudVendor> getAllVendors();
}
