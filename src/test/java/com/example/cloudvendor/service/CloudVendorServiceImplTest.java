package com.example.cloudvendor.service;

import com.example.cloudvendor.entity.CloudVendor;
import com.example.cloudvendor.repository.CloudVendorRepository;
import com.example.cloudvendor.service.impl.CloudVendorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Cloud Vendor Service Implementation Tests")
public class CloudVendorServiceImplTest {

    @Mock
    private CloudVendorRepository cloudVendorRepository;

    @InjectMocks
    private CloudVendorServiceImpl cloudVendorService;

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
    @DisplayName("Should create vendor successfully when vendor doesn't exist")
    void shouldCreateVendorSuccessfullyWhenVendorDoesNotExist() {
        // Given
        when(cloudVendorRepository.existsById("V001")).thenReturn(false);
        when(cloudVendorRepository.save(any(CloudVendor.class))).thenReturn(testVendor);

        // When
        CloudVendor result = cloudVendorService.createVendor(testVendor);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getVendorId()).isEqualTo("V001");
        assertThat(result.getVendorName()).isEqualTo("Amazon Web Services");
        assertThat(result.getVendorAddress()).isEqualTo("Seattle, WA, USA");
        assertThat(result.getVendorPhoneNumber()).isEqualTo("+1-206-266-1000");

        verify(cloudVendorRepository, times(1)).existsById("V001");
        verify(cloudVendorRepository, times(1)).save(testVendor);
    }

    @Test
    @DisplayName("Should throw exception when creating vendor that already exists")
    void shouldThrowExceptionWhenCreatingVendorThatAlreadyExists() {
        // Given
        when(cloudVendorRepository.existsById("V001")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> cloudVendorService.createVendor(testVendor))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cloud Vendor with ID V001 already exists");

        verify(cloudVendorRepository, times(1)).existsById("V001");
        verify(cloudVendorRepository, never()).save(any(CloudVendor.class));
    }

    @Test
    @DisplayName("Should update vendor successfully when vendor exists")
    void shouldUpdateVendorSuccessfullyWhenVendorExists() {
        // Given
        CloudVendor updatedVendor = new CloudVendor(
                "V001",
                "AWS - Updated",
                "Seattle, Washington, USA",
                "+1-206-266-2000"
        );
        when(cloudVendorRepository.existsById("V001")).thenReturn(true);
        when(cloudVendorRepository.save(any(CloudVendor.class))).thenReturn(updatedVendor);

        // When
        CloudVendor result = cloudVendorService.updateVendor(updatedVendor);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getVendorId()).isEqualTo("V001");
        assertThat(result.getVendorName()).isEqualTo("AWS - Updated");
        assertThat(result.getVendorAddress()).isEqualTo("Seattle, Washington, USA");
        assertThat(result.getVendorPhoneNumber()).isEqualTo("+1-206-266-2000");

        verify(cloudVendorRepository, times(1)).existsById("V001");
        verify(cloudVendorRepository, times(1)).save(updatedVendor);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent vendor")
    void shouldThrowExceptionWhenUpdatingNonExistentVendor() {
        // Given
        when(cloudVendorRepository.existsById("V001")).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> cloudVendorService.updateVendor(testVendor))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cloud Vendor with ID V001 not found");

        verify(cloudVendorRepository, times(1)).existsById("V001");
        verify(cloudVendorRepository, never()).save(any(CloudVendor.class));
    }

    @Test
    @DisplayName("Should delete vendor successfully when vendor exists")
    void shouldDeleteVendorSuccessfullyWhenVendorExists() {
        // Given
        when(cloudVendorRepository.existsById("V001")).thenReturn(true);
        doNothing().when(cloudVendorRepository).deleteById("V001");

        // When
        String result = cloudVendorService.deleteVendor("V001");

        // Then
        assertThat(result).isEqualTo("Cloud Vendor with ID V001 deleted successfully");

        verify(cloudVendorRepository, times(1)).existsById("V001");
        verify(cloudVendorRepository, times(1)).deleteById("V001");
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent vendor")
    void shouldThrowExceptionWhenDeletingNonExistentVendor() {
        // Given
        when(cloudVendorRepository.existsById("V001")).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> cloudVendorService.deleteVendor("V001"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cloud Vendor with ID V001 not found");

        verify(cloudVendorRepository, times(1)).existsById("V001");
        verify(cloudVendorRepository, never()).deleteById(anyString());
    }

    @Test
    @DisplayName("Should get vendor by ID successfully when vendor exists")
    void shouldGetVendorByIdSuccessfullyWhenVendorExists() {
        // Given
        when(cloudVendorRepository.findById("V001")).thenReturn(Optional.of(testVendor));

        // When
        CloudVendor result = cloudVendorService.getVendorById("V001");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getVendorId()).isEqualTo("V001");
        assertThat(result.getVendorName()).isEqualTo("Amazon Web Services");
        assertThat(result.getVendorAddress()).isEqualTo("Seattle, WA, USA");
        assertThat(result.getVendorPhoneNumber()).isEqualTo("+1-206-266-1000");

        verify(cloudVendorRepository, times(1)).findById("V001");
    }

    @Test
    @DisplayName("Should throw exception when getting non-existent vendor")
    void shouldThrowExceptionWhenGettingNonExistentVendor() {
        // Given
        when(cloudVendorRepository.findById("V001")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> cloudVendorService.getVendorById("V001"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cloud Vendor with ID V001 not found");

        verify(cloudVendorRepository, times(1)).findById("V001");
    }

    @Test
    @DisplayName("Should get all vendors successfully when vendors exist")
    void shouldGetAllVendorsSuccessfullyWhenVendorsExist() {
        // Given
        when(cloudVendorRepository.findAll()).thenReturn(testVendorList);

        // When
        List<CloudVendor> result = cloudVendorService.getAllVendors();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getVendorId()).isEqualTo("V001");
        assertThat(result.get(0).getVendorName()).isEqualTo("Amazon Web Services");
        assertThat(result.get(1).getVendorId()).isEqualTo("V002");
        assertThat(result.get(1).getVendorName()).isEqualTo("Microsoft Azure");

        verify(cloudVendorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should throw exception when no vendors exist")
    void shouldThrowExceptionWhenNoVendorsExist() {
        // Given
        when(cloudVendorRepository.findAll()).thenReturn(Collections.emptyList());

        // When & Then
        assertThatThrownBy(() -> cloudVendorService.getAllVendors())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No Cloud Vendors found");

        verify(cloudVendorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should handle null input gracefully in createVendor")
    void shouldHandleNullInputGracefullyInCreateVendor() {
        // When & Then
        assertThatThrownBy(() -> cloudVendorService.createVendor(null))
                .isInstanceOf(NullPointerException.class);

        verify(cloudVendorRepository, never()).existsById(anyString());
        verify(cloudVendorRepository, never()).save(any(CloudVendor.class));
    }

    @Test
    @DisplayName("Should handle null input gracefully in updateVendor")
    void shouldHandleNullInputGracefullyInUpdateVendor() {
        // When & Then
        assertThatThrownBy(() -> cloudVendorService.updateVendor(null))
                .isInstanceOf(NullPointerException.class);

        verify(cloudVendorRepository, never()).existsById(anyString());
        verify(cloudVendorRepository, never()).save(any(CloudVendor.class));
    }

    @Test
    @DisplayName("Should handle null input gracefully in deleteVendor")
    void shouldHandleNullInputGracefullyInDeleteVendor() {
        // When & Then
        assertThatThrownBy(() -> cloudVendorService.deleteVendor(null))
                .isInstanceOf(RuntimeException.class);

        verify(cloudVendorRepository, never()).deleteById(anyString());
    }

    @Test
    @DisplayName("Should handle null input gracefully in getVendorById")
    void shouldHandleNullInputGracefullyInGetVendorById() {
        // When & Then
        assertThatThrownBy(() -> cloudVendorService.getVendorById(null))
                .isInstanceOf(RuntimeException.class);

        verify(cloudVendorRepository, never()).findById(anyString());
    }
}
