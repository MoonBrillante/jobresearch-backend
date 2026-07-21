package com.example.jobresearch.controllers;

import com.example.jobresearch.domain.models.Job;
import com.example.jobresearch.domain.services.JobService;
import com.example.jobresearch.repositories.AppUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobController.class)
// Security filters are disabled here because JWTAuthorizationFilter
// is already covered by its own dedicated unit tests.
@AutoConfigureMockMvc(addFilters = false)
class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JobService jobService;

    // The application seeds test users on startup when the user table is empty.
    // Since TESTUSER_PW and ADMINUSER_PW are not available during tests,
    // this test configuration provides a mocked repository with count() returning
    // a non-zero value, preventing the seeding logic from running.
    @TestConfiguration
    static class MockRepositoryConfig {
        @Bean
        AppUserRepository appUserRepository() {
            AppUserRepository mock = Mockito.mock(AppUserRepository.class);
            when(mock.count()).thenReturn(1L);
            return mock;
        }
    }

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getJobById_jobExists_returns200WithJob() throws Exception {
        // Arrange
        Job job = new Job();
        job.setId(1L);
        job.setPosition("Backend Developer");
        job.setCompany("TestCorp");

        when(jobService.getJobById(1L)).thenReturn(Optional.of(job));

        // Act + Assert
        mockMvc.perform(get("/api/jobs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.position").value("Backend Developer"))
                .andExpect(jsonPath("$.company").value("TestCorp"));
    }

    @Test
    void getJobById_jobDoesNotExist_returns404() throws Exception {
        // Arrange
        when(jobService.getJobById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        mockMvc.perform(get("/api/jobs/99"))
                .andExpect(status().isNotFound());
    }
}