package com.example.cloudvendor.repository;

import com.example.cloudvendor.entity.CloudVendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CloudVendorRepository extends JpaRepository<CloudVendor, String> {
    
    // Custom query methods can be added here if needed
    // For example:
    // List<CloudVendor> findByVendorName(String vendorName);
    // List<CloudVendor> findByVendorAddress(String vendorAddress);
}
