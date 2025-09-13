-- Cloud Vendor API Database Setup Script
-- This script creates the database and table structure for the Cloud Vendor API

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS cloudvendor_db;

-- Use the database
USE cloudvendor_db;

-- Create cloud_vendor table
CREATE TABLE IF NOT EXISTS cloud_vendor (
    vendor_id VARCHAR(255) NOT NULL PRIMARY KEY,
    vendor_name VARCHAR(255) NOT NULL,
    vendor_address VARCHAR(500) NOT NULL,
    vendor_phone_number VARCHAR(50) NOT NULL
);

-- Insert sample data
INSERT INTO cloud_vendor (vendor_id, vendor_name, vendor_address, vendor_phone_number) VALUES
('AWS001', 'Amazon Web Services', '410 Terry Ave N, Seattle, WA 98109, USA', '+1-206-266-1000'),
('GCP001', 'Google Cloud Platform', '1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA', '+1-650-253-0000'),
('AZ001', 'Microsoft Azure', 'One Microsoft Way, Redmond, WA 98052, USA', '+1-425-882-8080'),
('IBM001', 'IBM Cloud', '1 New Orchard Rd, Armonk, NY 10504, USA', '+1-914-499-1900'),
('ORA001', 'Oracle Cloud', '500 Oracle Pkwy, Redwood City, CA 94065, USA', '+1-650-506-7000')
ON DUPLICATE KEY UPDATE 
    vendor_name = VALUES(vendor_name),
    vendor_address = VALUES(vendor_address),
    vendor_phone_number = VALUES(vendor_phone_number);

-- Query to verify the data
SELECT * FROM cloud_vendor;

-- Show table structure
DESCRIBE cloud_vendor;
