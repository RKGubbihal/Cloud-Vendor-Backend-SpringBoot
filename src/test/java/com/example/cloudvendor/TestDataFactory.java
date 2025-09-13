package com.example.cloudvendor;

import com.example.cloudvendor.entity.CloudVendor;

import java.util.Arrays;
import java.util.List;

/**
 * Test Data Factory for creating consistent test data across all test classes
 */
public class TestDataFactory {

    public static CloudVendor createValidCloudVendor() {
        return new CloudVendor(
                "V001",
                "Amazon Web Services",
                "Seattle, WA, USA",
                "+1-206-266-1000"
        );
    }

    public static CloudVendor createValidCloudVendor(String id) {
        return new CloudVendor(
                id,
                "Test Vendor " + id,
                "Test Address " + id,
                "+1-000-000-" + id.substring(1)
        );
    }

    public static CloudVendor createAzureVendor() {
        return new CloudVendor(
                "V002",
                "Microsoft Azure",
                "Redmond, WA, USA",
                "+1-425-882-8080"
        );
    }

    public static CloudVendor createGoogleCloudVendor() {
        return new CloudVendor(
                "V003",
                "Google Cloud Platform",
                "Mountain View, CA, USA",
                "+1-650-253-0000"
        );
    }

    public static CloudVendor createUpdatedCloudVendor() {
        return new CloudVendor(
                "V001",
                "AWS - Updated",
                "Seattle, Washington, USA",
                "+1-206-266-2000"
        );
    }

    public static CloudVendor createInvalidCloudVendor() {
        return new CloudVendor("", "", "", "");
    }

    public static CloudVendor createCloudVendorWithBlankId() {
        return new CloudVendor("", "Valid Name", "Valid Address", "Valid Phone");
    }

    public static CloudVendor createCloudVendorWithBlankName() {
        return new CloudVendor("V001", "", "Valid Address", "Valid Phone");
    }

    public static CloudVendor createCloudVendorWithBlankAddress() {
        return new CloudVendor("V001", "Valid Name", "", "Valid Phone");
    }

    public static CloudVendor createCloudVendorWithBlankPhone() {
        return new CloudVendor("V001", "Valid Name", "Valid Address", "");
    }

    public static List<CloudVendor> createMultipleCloudVendors() {
        return Arrays.asList(
                createValidCloudVendor(),
                createAzureVendor(),
                createGoogleCloudVendor()
        );
    }

    public static List<CloudVendor> createTwoCloudVendors() {
        return Arrays.asList(
                createValidCloudVendor(),
                createAzureVendor()
        );
    }

    // Common test constants
    public static class Constants {
        public static final String VENDOR_NOT_FOUND_MESSAGE = "Cloud Vendor with ID %s not found";
        public static final String VENDOR_ALREADY_EXISTS_MESSAGE = "Cloud Vendor with ID %s already exists";
        public static final String VENDOR_DELETED_MESSAGE = "Cloud Vendor with ID %s deleted successfully";
        public static final String NO_VENDORS_FOUND_MESSAGE = "No Cloud Vendors found";
        public static final String HEALTH_CHECK_MESSAGE = "Cloud Vendor API is running successfully!";
    }

    // Helper methods for creating error messages
    public static String vendorNotFoundMessage(String vendorId) {
        return String.format(Constants.VENDOR_NOT_FOUND_MESSAGE, vendorId);
    }

    public static String vendorAlreadyExistsMessage(String vendorId) {
        return String.format(Constants.VENDOR_ALREADY_EXISTS_MESSAGE, vendorId);
    }

    public static String vendorDeletedMessage(String vendorId) {
        return String.format(Constants.VENDOR_DELETED_MESSAGE, vendorId);
    }
}
