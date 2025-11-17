package com.example.cloudvendor.integration;

import com.example.cloudvendor.entity.CloudVendor;
import com.example.cloudvendor.repository.CloudVendorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Cloud Vendor Integration Tests")
public class CloudVendorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CloudVendorRepository cloudVendorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private CloudVendor testVendor;

    @BeforeEach
    void setUp() {
        cloudVendorRepository.deleteAll();
        
        testVendor = new CloudVendor(
                "V001",
                "Amazon Web Services",
                "Seattle, WA, USA",
                "+1-206-266-1000"
        );
    }

    @Test
    @DisplayName("Should perform complete CRUD operations successfully")
    void shouldPerformCompleteCrudOperationsSuccessfully() throws Exception {
        // 1. CREATE - Create a new vendor
        mockMvc.perform(post("/cloudvendor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testVendor)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.vendorId", is("V001")))
                .andExpect(jsonPath("$.vendorName", is("Amazon Web Services")));

        // 2. READ - Get the created vendor
        mockMvc.perform(get("/cloudvendor/V001"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendorId", is("V001")))
                .andExpect(jsonPath("$.vendorName", is("Amazon Web Services")))
                .andExpect(jsonPath("$.vendorAddress", is("Seattle, WA, USA")))
                .andExpect(jsonPath("$.vendorPhoneNumber", is("+1-206-266-1000")));

        // 3. UPDATE - Update the vendor
        CloudVendor updatedVendor = new CloudVendor(
                "V001",
                "AWS - Updated",
                "Seattle, Washington, USA",
                "+1-206-266-2000"
        );

        mockMvc.perform(put("/cloudvendor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedVendor)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendorId", is("V001")))
                .andExpect(jsonPath("$.vendorName", is("AWS - Updated")))
                .andExpect(jsonPath("$.vendorAddress", is("Seattle, Washington, USA")))
                .andExpect(jsonPath("$.vendorPhoneNumber", is("+1-206-266-2000")));

        // 4. READ - Verify the update
        mockMvc.perform(get("/cloudvendor/V001"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendorName", is("AWS - Updated")));

        // 5. DELETE - Delete the vendor
        mockMvc.perform(delete("/cloudvendor/V001"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Cloud Vendor with ID V001 deleted successfully"));

        // 6. READ - Verify deletion
        mockMvc.perform(get("/cloudvendor/V001"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should handle multiple vendors correctly")
    void shouldHandleMultipleVendorsCorrectly() throws Exception {
        // Create first vendor
        mockMvc.perform(post("/cloudvendor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testVendor)))
                .andExpect(status().isCreated());

        // Create second vendor
        CloudVendor vendor2 = new CloudVendor(
                "V002",
                "Microsoft Azure",
                "Redmond, WA, USA",
                "+1-425-882-8080"
        );

        mockMvc.perform(post("/cloudvendor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vendor2)))
                .andExpect(status().isCreated());

        // Get all vendors
        mockMvc.perform(get("/cloudvendor"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].vendorId", is("V001")))
                .andExpect(jsonPath("$[1].vendorId", is("V002")));

        // Delete one vendor
        mockMvc.perform(delete("/cloudvendor/V001"))
                .andExpect(status().isOk());

        // Verify only one vendor remains
        mockMvc.perform(get("/cloudvendor"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].vendorId", is("V002")));
    }

    @Test
    @DisplayName("Should validate input data correctly")
    void shouldValidateInputDataCorrectly() throws Exception {
        // Test with blank vendor ID
        CloudVendor invalidVendor = new CloudVendor("", "Test Vendor", "Test Address", "123-456-7890");

        mockMvc.perform(post("/cloudvendor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidVendor)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        // Test with blank vendor name
        invalidVendor = new CloudVendor("V001", "", "Test Address", "123-456-7890");

        mockMvc.perform(post("/cloudvendor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidVendor)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        // Test with blank address
        invalidVendor = new CloudVendor("V001", "Test Vendor", "", "123-456-7890");

        mockMvc.perform(post("/cloudvendor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidVendor)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        // Test with blank phone number
        invalidVendor = new CloudVendor("V001", "Test Vendor", "Test Address", "");

        mockMvc.perform(post("/cloudvendor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidVendor)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle duplicate vendor creation")
    void shouldHandleDuplicateVendorCreation() throws Exception {
        // Create first vendor
        mockMvc.perform(post("/cloudvendor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testVendor)))
                .andExpect(status().isCreated());

        // Try to create duplicate vendor
        mockMvc.perform(post("/cloudvendor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testVendor)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cloud Vendor with ID V001 already exists"));
    }

    @Test
    @DisplayName("Should handle non-existent vendor operations")
    void shouldHandleNonExistentVendorOperations() throws Exception {
        // Try to get non-existent vendor
        mockMvc.perform(get("/cloudvendor/V999"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cloud Vendor with ID V999 not found"));

        // Try to update non-existent vendor
        CloudVendor nonExistentVendor = new CloudVendor("V999", "Test", "Test", "Test");
        mockMvc.perform(put("/cloudvendor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonExistentVendor)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cloud Vendor with ID V999 not found"));

        // Try to delete non-existent vendor
        mockMvc.perform(delete("/cloudvendor/V999"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cloud Vendor with ID V999 not found"));
    }

    @Test
    @DisplayName("Should handle empty vendor list")
    void shouldHandleEmptyVendorList() throws Exception {
        // Try to get all vendors when none exist
        mockMvc.perform(get("/cloudvendor"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("No Cloud Vendors found"));
    }

    @Test
    @DisplayName("Should test health endpoint")
    void shouldTestHealthEndpoint() throws Exception {
        mockMvc.perform(get("/cloudvendor/health"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Cloud Vendor API is running successfully!"));
    }

    @Test
    @DisplayName("Should handle malformed JSON requests")
    void shouldHandleMalformedJsonRequests() throws Exception {
        String malformedJson = "{\"vendorId\":\"V001\",\"vendorName\":}";

        mockMvc.perform(post("/cloudvendor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle missing content type")
    void shouldHandleMissingContentType() throws Exception {
        mockMvc.perform(post("/cloudvendor")
                        .content(objectMapper.writeValueAsString(testVendor)))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @DisplayName("Should handle unsupported HTTP methods")
    void shouldHandleUnsupportedHttpMethods() throws Exception {
        // PATCH is not supported
        mockMvc.perform(patch("/cloudvendor/V001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testVendor)))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("Should maintain data consistency across operations")
    void shouldMaintainDataConsistencyAcrossOperations() throws Exception {
        // Create vendor
        mockMvc.perform(post("/cloudvendor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testVendor)))
                .andExpect(status().isCreated());

        // Verify it exists in database
        assert cloudVendorRepository.existsById("V001");
        assert cloudVendorRepository.count() == 1;

        // Update vendor
        CloudVendor updatedVendor = new CloudVendor("V001", "Updated Name", "Updated Address", "Updated Phone");
        mockMvc.perform(put("/cloudvendor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedVendor)))
                .andExpect(status().isOk());

        // Verify update in database
        CloudVendor dbVendor = cloudVendorRepository.findById("V001").orElse(null);
        assert dbVendor != null;
        assert "Updated Name".equals(dbVendor.getVendorName());
        assert cloudVendorRepository.count() == 1; // Still only one record

        // Delete vendor
        mockMvc.perform(delete("/cloudvendor/V001"))
                .andExpect(status().isOk());

        // Verify deletion in database
        assert !cloudVendorRepository.existsById("V001");
        assert cloudVendorRepository.count() == 0;
    }
}
