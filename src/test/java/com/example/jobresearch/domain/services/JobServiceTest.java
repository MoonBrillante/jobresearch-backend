package com.example.jobresearch.domain.services;

import com.example.jobresearch.domain.models.Job;
import com.example.jobresearch.repositories.JobRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    @Test
    void getFilteredJobs_invalidSortBy_fallsBackToPostedDate() {
        // Arrange: mock returns an empty page; we only care about the arguments passed in
        when(jobRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        // Act: intentionally pass a sortBy field that is NOT in the allowed list
        jobService.getFilteredJobs(0, 10, "hackedField", "desc",
                null, null, null, null, null);

        // Assert: capture the Pageable passed to the repository and check the sort field
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(jobRepository).findAll(any(Specification.class), pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        assertNotNull(capturedPageable.getSort().getOrderFor("postedDate"),
                "Invalid sortBy should fall back to postedDate");
    }

    @Test
    void getFilteredJobs_ascendingSort_isApplied() {
        // Arrange
        when(jobRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        // Act
        jobService.getFilteredJobs(0, 10, "company", "asc",
                null, null, null, null, null);

        // Assert
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(jobRepository).findAll(any(Specification.class), pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        assertTrue(capturedPageable.getSort().getOrderFor("company").isAscending(),
                "sortDir=asc should produce ascending order");
    }

    @Test
    void updateJob_jobNotFound_throwsException() {
        // Arrange
        when(jobRepository.findById(99L)).thenReturn(Optional.empty());
        Job dummyUpdate = new Job();

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> jobService.updateJob(99L, dummyUpdate),
                "Updating a non-existent job should throw an exception");
    }
}