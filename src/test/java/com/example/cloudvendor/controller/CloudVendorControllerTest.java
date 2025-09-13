package com.example.cloudvendor.controller;

import com.example.cloudvendor.entity.CloudVendor;
import com.example.cloudvendor.service.CloudVendorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CloudVendorController.class)
@DisplayName("Cloud Vendor Controller Tests")
public class CloudVendorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CloudVendorService cloudVendorService;

    @Autowired
    private ObjectMapper objectMapper;

    private CloudVendor testVendor;
    private List<CloudVendor> testVendorList;

    @BeforeEach
    void setUp() {
        testVendor = new CloudVendor(
                "V001",
                "Amazon Web Services",
                "Seattle, WA, USA",
                "+1-206-266-1000"
        );

        CloudVendor vendor2 = new CloudVendor(
                "V002",
                "Microsoft Azure",
                "Redmond, WA, USA",
                "+1-425-882-8080"
        );

        testVendorList = Arrays.asList(testVendor, vendor2);
    }

    @Test
    @DisplayName("Should create cloud vendor successfully")
    void shouldCreateCloudVendorSuccessfully() throws Exception {
        // Given
        when(cloudVendorService.createVendor(any(CloudVendor.class))).thenReturn(testVendor);

        // When & Then
        mockMvc.perform(post("/cloudvendor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testVendor)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.vendorId", is("V001")))
                .andExpect(jsonPath("$.vendorName", is("Amazon Web Services")))
                .andExpect(jsonPath("$.vendorAddress", is("Seattle, WA, USA")))
                .andExpect(jsonPath("$.vendorPhoneNumber", is("+1-206-266-1000")));

        verify(cloudVendorService, times(1)).createVendor(any(CloudVendor.class));
    }

    @Test
    @DisplayName("Should return bad request when creating vendor with invalid data")
    void shouldReturnBadRequestWhenCreatingVendorWithInvalidData() throws Exception {
        // Given
        CloudVendor invalidVendor = new CloudVendor("", "", "", "");

        // When & Then
        mockMvc.perform(post("/cloudvendor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidVendor)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(cloudVendorService, never()).createVendor(any(CloudVendor.class));
    }

    @Test
    @DisplayName("Should return bad request when vendor already exists")
    void shouldReturnBadRequestWhenVendorAlreadyExists() throws Exception {
        // Given
        when(cloudVendorService.createVendor(any(CloudVendor.class)))
                .thenThrow(new RuntimeException("Cloud Vendor with ID V001 already exists"));

        // When & Then
        mockMvc.perform(post("/cloudvendor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testVendor)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cloud Vendor with ID V001 already exists"));

        verify(cloudVendorService, times(1)).createVendor(any(CloudVendor.class));
    }

    @Test
    @DisplayName("Should get cloud vendor by ID successfully")
    void shouldGetCloudVendorByIdSuccessfully() throws Exception {
        // Given
        when(cloudVendorService.getVendorById("V001")).thenReturn(testVendor);

        // When & Then
        mockMvc.perform(get("/cloudvendor/V001"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.vendorId", is("V001")))
                .andExpect(jsonPath("$.vendorName", is("Amazon Web Services")))
                .andExpect(jsonPath("$.vendorAddress", is("Seattle, WA, USA")))
                .andExpect(jsonPath("$.vendorPhoneNumber", is("+1-206-266-1000")));

        verify(cloudVendorService, times(1)).getVendorById("V001");
    }

    @Test
    @DisplayName("Should return not found when getting non-existent vendor")
    void shouldReturnNotFoundWhenGettingNonExistentVendor() throws Exception {
        // Given
        when(cloudVendorService.getVendorById("V999"))
                .thenThrow(new RuntimeException("Cloud Vendor with ID V999 not found"));

        // When & Then
        mockMvc.perform(get("/cloudvendor/V999"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cloud Vendor with ID V999 not found"));

        verify(cloudVendorService, times(1)).getVendorById("V999");
    }

    @Test
    @DisplayName("Should get all cloud vendors successfully")
    void shouldGetAllCloudVendorsSuccessfully() throws Exception {
        // Given
        when(cloudVendorService.getAllVendors()).thenReturn(testVendorList);

        // When & Then
        mockMvc.perform(get("/cloudvendor"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].vendorId", is("V001")))
                .andExpect(jsonPath("$[0].vendorName", is("Amazon Web Services")))
                .andExpect(jsonPath("$[1].vendorId", is("V002")))
                .andExpect(jsonPath("$[1].vendorName", is("Microsoft Azure")));

        verify(cloudVendorService, times(1)).getAllVendors();
    }

    @Test
    @DisplayName("Should return not found when no vendors exist")
    void shouldReturnNotFoundWhenNoVendorsExist() throws Exception {
        // Given
        when(cloudVendorService.getAllVendors())
                .thenThrow(new RuntimeException("No Cloud Vendors found"));

        // When & Then
        mockMvc.perform(get("/cloudvendor"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("No Cloud Vendors found"));

        verify(cloudVendorService, times(1)).getAllVendors();
    }

    @Test
    @DisplayName("Should update cloud vendor successfully")
    void shouldUpdateCloudVendorSuccessfully() throws Exception {
        // Given
        CloudVendor updatedVendor = new CloudVendor(
                "V001",
                "AWS - Updated",
                "Seattle, Washington, USA",
                "+1-206-266-2000"
        );
        when(cloudVendorService.updateVendor(any(CloudVendor.class))).thenReturn(updatedVendor);

        // When & Then
        mockMvc.perform(put("/cloudvendor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedVendor)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.vendorId", is("V001")))
                .andExpect(jsonPath("$.vendorName", is("AWS - Updated")))
                .andExpect(jsonPath("$.vendorAddress", is("Seattle, Washington, USA")))
                .andExpect(jsonPath("$.vendorPhoneNumber", is("+1-206-266-2000")));

        verify(cloudVendorService, times(1)).updateVendor(any(CloudVendor.class));
    }

    @Test
    @DisplayName("Should return not found when updating non-existent vendor")
    void shouldReturnNotFoundWhenUpdatingNonExistentVendor() throws Exception {
        // Given
        when(cloudVendorService.updateVendor(any(CloudVendor.class)))
                .thenThrow(new RuntimeException("Cloud Vendor with ID V001 not found"));

        // When & Then
        mockMvc.perform(put("/cloudvendor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testVendor)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cloud Vendor with ID V001 not found"));

        verify(cloudVendorService, times(1)).updateVendor(any(CloudVendor.class));
    }

    @Test
    @DisplayName("Should return bad request when updating with invalid data")
    void shouldReturnBadRequestWhenUpdatingWithInvalidData() throws Exception {
        // Given
        CloudVendor invalidVendor = new CloudVendor("V001", "", "", "");

        // When & Then
        mockMvc.perform(put("/cloudvendor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidVendor)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(cloudVendorService, never()).updateVendor(any(CloudVendor.class));
    }

    @Test
    @DisplayName("Should delete cloud vendor successfully")
    void shouldDeleteCloudVendorSuccessfully() throws Exception {
        // Given
        String successMessage = "Cloud Vendor with ID V001 deleted successfully";
        when(cloudVendorService.deleteVendor("V001")).thenReturn(successMessage);

        // When & Then
        mockMvc.perform(delete("/cloudvendor/V001"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(successMessage));

        verify(cloudVendorService, times(1)).deleteVendor("V001");
    }

    @Test
    @DisplayName("Should return not found when deleting non-existent vendor")
    void shouldReturnNotFoundWhenDeletingNonExistentVendor() throws Exception {
        // Given
        when(cloudVendorService.deleteVendor("V999"))
                .thenThrow(new RuntimeException("Cloud Vendor with ID V999 not found"));

        // When & Then
        mockMvc.perform(delete("/cloudvendor/V999"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cloud Vendor with ID V999 not found"));

        verify(cloudVendorService, times(1)).deleteVendor("V999");
    }

    @Test
    @DisplayName("Should return success for health check")
    void shouldReturnSuccessForHealthCheck() throws Exception {
        // When & Then
        mockMvc.perform(get("/cloudvendor/health"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Cloud Vendor API is running successfully!"));

        verify(cloudVendorService, never()).getAllVendors();
    }
}
