package com.example.cloudvendor.controller;

import com.example.cloudvendor.entity.CloudVendor;
import com.example.cloudvendor.service.CloudVendorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cloudvendor")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Cloud Vendor Management", description = "APIs for managing cloud vendors")
public class CloudVendorController {
    
    private final CloudVendorService cloudVendorService;
    
    @Operation(
            summary = "Create a new cloud vendor",
            description = "Creates a new cloud vendor with the provided details"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Cloud vendor created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CloudVendor.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input or vendor already exists",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            )
    })
    @PostMapping
    public ResponseEntity<?> createVendor(
            @Parameter(description = "Cloud vendor details to create", required = true)
            @Valid @RequestBody CloudVendor cloudVendor) {
        try {
            CloudVendor createdVendor = cloudVendorService.createVendor(cloudVendor);
            return new ResponseEntity<>(createdVendor, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @Operation(
            summary = "Get cloud vendor by ID",
            description = "Retrieves a cloud vendor by their unique vendor ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cloud vendor found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CloudVendor.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cloud vendor not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            )
    })
    @GetMapping("/{vendorId}")
    public ResponseEntity<?> getVendorById(
            @Parameter(description = "Unique identifier of the cloud vendor", required = true, example = "V001")
            @PathVariable String vendorId) {
        try {
            CloudVendor vendor = cloudVendorService.getVendorById(vendorId);
            return new ResponseEntity<>(vendor, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @Operation(
            summary = "Get all cloud vendors",
            description = "Retrieves a list of all cloud vendors in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of cloud vendors retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CloudVendor.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No cloud vendors found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            )
    })
    @GetMapping
    public ResponseEntity<?> getAllVendors() {
        try {
            List<CloudVendor> vendors = cloudVendorService.getAllVendors();
            return new ResponseEntity<>(vendors, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @Operation(
            summary = "Update an existing cloud vendor",
            description = "Updates an existing cloud vendor with new details"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cloud vendor updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CloudVendor.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cloud vendor not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            )
    })
    @PutMapping
    public ResponseEntity<?> updateVendor(
            @Parameter(description = "Updated cloud vendor details", required = true)
            @Valid @RequestBody CloudVendor cloudVendor) {
        try {
            CloudVendor updatedVendor = cloudVendorService.updateVendor(cloudVendor);
            return new ResponseEntity<>(updatedVendor, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @Operation(
            summary = "Delete a cloud vendor by ID",
            description = "Deletes a cloud vendor from the system using their unique vendor ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cloud vendor deleted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cloud vendor not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            )
    })
    @DeleteMapping("/{vendorId}")
    public ResponseEntity<?> deleteVendor(
            @Parameter(description = "Unique identifier of the cloud vendor to delete", required = true, example = "V001")
            @PathVariable String vendorId) {
        try {
            String message = cloudVendorService.deleteVendor(vendorId);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @Operation(
            summary = "Health check endpoint",
            description = "Checks if the Cloud Vendor API is running and healthy"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "API is healthy and running",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            )
    })
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return new ResponseEntity<>("Cloud Vendor API is running successfully!", HttpStatus.OK);
    }
}
