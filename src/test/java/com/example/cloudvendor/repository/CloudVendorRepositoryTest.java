package com.example.cloudvendor.repository;

import com.example.cloudvendor.entity.CloudVendor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Cloud Vendor Repository Tests")
public class CloudVendorRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CloudVendorRepository cloudVendorRepository;

    private CloudVendor testVendor1;
    private CloudVendor testVendor2;

    @BeforeEach
    void setUp() {
        testVendor1 = new CloudVendor(
                "V001",
                "Amazon Web Services",
                "Seattle, WA, USA",
                "+1-206-266-1000"
        );

        testVendor2 = new CloudVendor(
                "V002",
                "Microsoft Azure",
                "Redmond, WA, USA",
                "+1-425-882-8080"
        );
    }

    @Test
    @DisplayName("Should save cloud vendor successfully")
    void shouldSaveCloudVendorSuccessfully() {
        // When
        CloudVendor savedVendor = cloudVendorRepository.save(testVendor1);

        // Then
        assertThat(savedVendor).isNotNull();
        assertThat(savedVendor.getVendorId()).isEqualTo("V001");
        assertThat(savedVendor.getVendorName()).isEqualTo("Amazon Web Services");
        assertThat(savedVendor.getVendorAddress()).isEqualTo("Seattle, WA, USA");
        assertThat(savedVendor.getVendorPhoneNumber()).isEqualTo("+1-206-266-1000");

        // Verify it's persisted
        CloudVendor foundVendor = entityManager.find(CloudVendor.class, "V001");
        assertThat(foundVendor).isNotNull();
        assertThat(foundVendor.getVendorName()).isEqualTo("Amazon Web Services");
    }

    @Test
    @DisplayName("Should find cloud vendor by ID successfully")
    void shouldFindCloudVendorByIdSuccessfully() {
        // Given
        entityManager.persistAndFlush(testVendor1);

        // When
        Optional<CloudVendor> foundVendor = cloudVendorRepository.findById("V001");

        // Then
        assertThat(foundVendor).isPresent();
        assertThat(foundVendor.get().getVendorId()).isEqualTo("V001");
        assertThat(foundVendor.get().getVendorName()).isEqualTo("Amazon Web Services");
        assertThat(foundVendor.get().getVendorAddress()).isEqualTo("Seattle, WA, USA");
        assertThat(foundVendor.get().getVendorPhoneNumber()).isEqualTo("+1-206-266-1000");
    }

    @Test
    @DisplayName("Should return empty when cloud vendor not found by ID")
    void shouldReturnEmptyWhenCloudVendorNotFoundById() {
        // When
        Optional<CloudVendor> foundVendor = cloudVendorRepository.findById("V999");

        // Then
        assertThat(foundVendor).isEmpty();
    }

    @Test
    @DisplayName("Should find all cloud vendors successfully")
    void shouldFindAllCloudVendorsSuccessfully() {
        // Given
        entityManager.persistAndFlush(testVendor1);
        entityManager.persistAndFlush(testVendor2);

        // When
        List<CloudVendor> vendors = cloudVendorRepository.findAll();

        // Then
        assertThat(vendors).isNotNull();
        assertThat(vendors).hasSize(2);
        assertThat(vendors).extracting(CloudVendor::getVendorId)
                .containsExactlyInAnyOrder("V001", "V002");
        assertThat(vendors).extracting(CloudVendor::getVendorName)
                .containsExactlyInAnyOrder("Amazon Web Services", "Microsoft Azure");
    }

    @Test
    @DisplayName("Should return empty list when no cloud vendors exist")
    void shouldReturnEmptyListWhenNoCloudVendorsExist() {
        // When
        List<CloudVendor> vendors = cloudVendorRepository.findAll();

        // Then
        assertThat(vendors).isNotNull();
        assertThat(vendors).isEmpty();
    }

    @Test
    @DisplayName("Should check if cloud vendor exists by ID")
    void shouldCheckIfCloudVendorExistsById() {
        // Given
        entityManager.persistAndFlush(testVendor1);

        // When & Then
        assertThat(cloudVendorRepository.existsById("V001")).isTrue();
        assertThat(cloudVendorRepository.existsById("V999")).isFalse();
    }

    @Test
    @DisplayName("Should delete cloud vendor by ID successfully")
    void shouldDeleteCloudVendorByIdSuccessfully() {
        // Given
        entityManager.persistAndFlush(testVendor1);
        assertThat(cloudVendorRepository.existsById("V001")).isTrue();

        // When
        cloudVendorRepository.deleteById("V001");

        // Then
        assertThat(cloudVendorRepository.existsById("V001")).isFalse();
        Optional<CloudVendor> deletedVendor = cloudVendorRepository.findById("V001");
        assertThat(deletedVendor).isEmpty();
    }

    @Test
    @DisplayName("Should update cloud vendor successfully")
    void shouldUpdateCloudVendorSuccessfully() {
        // Given
        entityManager.persistAndFlush(testVendor1);

        // When
        CloudVendor vendorToUpdate = cloudVendorRepository.findById("V001").orElse(null);
        assertThat(vendorToUpdate).isNotNull();
        
        vendorToUpdate.setVendorName("AWS - Updated");
        vendorToUpdate.setVendorAddress("Seattle, Washington, USA");
        vendorToUpdate.setVendorPhoneNumber("+1-206-266-2000");
        
        CloudVendor updatedVendor = cloudVendorRepository.save(vendorToUpdate);

        // Then
        assertThat(updatedVendor).isNotNull();
        assertThat(updatedVendor.getVendorId()).isEqualTo("V001");
        assertThat(updatedVendor.getVendorName()).isEqualTo("AWS - Updated");
        assertThat(updatedVendor.getVendorAddress()).isEqualTo("Seattle, Washington, USA");
        assertThat(updatedVendor.getVendorPhoneNumber()).isEqualTo("+1-206-266-2000");

        // Verify persistence
        CloudVendor persistedVendor = entityManager.find(CloudVendor.class, "V001");
        assertThat(persistedVendor.getVendorName()).isEqualTo("AWS - Updated");
    }

    @Test
    @DisplayName("Should handle duplicate ID constraint")
    void shouldHandleDuplicateIdConstraint() {
        // Given
        entityManager.persistAndFlush(testVendor1);

        // When & Then
        CloudVendor duplicateVendor = new CloudVendor(
                "V001", // Same ID
                "Google Cloud Platform",
                "Mountain View, CA, USA",
                "+1-650-253-0000"
        );

        // This should work as save() will update the existing entity
        CloudVendor savedVendor = cloudVendorRepository.save(duplicateVendor);
        assertThat(savedVendor.getVendorName()).isEqualTo("Google Cloud Platform");

        // Verify only one entity exists
        List<CloudVendor> allVendors = cloudVendorRepository.findAll();
        assertThat(allVendors).hasSize(1);
        assertThat(allVendors.get(0).getVendorName()).isEqualTo("Google Cloud Platform");
    }

    @Test
    @DisplayName("Should count cloud vendors correctly")
    void shouldCountCloudVendorsCorrectly() {
        // Given - no vendors initially
        assertThat(cloudVendorRepository.count()).isEqualTo(0);

        // When - add vendors
        entityManager.persistAndFlush(testVendor1);
        assertThat(cloudVendorRepository.count()).isEqualTo(1);

        entityManager.persistAndFlush(testVendor2);
        assertThat(cloudVendorRepository.count()).isEqualTo(2);

        // When - delete one vendor
        cloudVendorRepository.deleteById("V001");
        assertThat(cloudVendorRepository.count()).isEqualTo(1);

        // When - delete all vendors
        cloudVendorRepository.deleteAll();
        assertThat(cloudVendorRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should handle entity validation constraints")
    void shouldHandleEntityValidationConstraints() {
        // Given - vendor with blank fields (should be handled by validation)
        CloudVendor invalidVendor = new CloudVendor("", "", "", "");

        // When & Then - This will succeed in repository layer
        // Validation happens at the service/controller layer
        CloudVendor savedVendor = cloudVendorRepository.save(invalidVendor);
        assertThat(savedVendor).isNotNull();
        assertThat(savedVendor.getVendorId()).isEmpty();
    }
}
