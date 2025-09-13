package com.example.cloudvendor.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cloud_vendor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CloudVendor {
    
    @Id
    @Column(name = "vendor_id")
    @NotBlank(message = "Vendor ID cannot be blank")
    private String vendorId;
    
    @Column(name = "vendor_name")
    @NotBlank(message = "Vendor name cannot be blank")
    private String vendorName;
    
    @Column(name = "vendor_address")
    @NotBlank(message = "Vendor address cannot be blank")
    private String vendorAddress;
    
    @Column(name = "vendor_phone_number")
    @NotBlank(message = "Vendor phone number cannot be blank")
    private String vendorPhoneNumber;
}
