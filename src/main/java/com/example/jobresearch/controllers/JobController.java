package com.example.jobresearch.controllers;

import com.example.jobresearch.domain.models.Job;
import com.example.jobresearch.domain.services.JobService;
import com.example.jobresearch.domain.models.JobStatus;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import io.swagger.v3.oas.annotations.*;
import org.springframework.data.domain.Page;

/**
 * JobController
 * Handles CRUD operations for Job resources.
 * Base path: /api/jobs
 */

@RestController
@RequestMapping("/api/jobs")
@Tag(name = "Job Controller", description = "Operations related to job postings")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @Operation(summary = "Get all job listings", description = "Returns a list of all jobs.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }


    @Operation(
            summary = "Get filtered jobs by column",
            description = "Returns paginated job listings filtered by individual columns (position, company, location, mode, status)."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved filtered jobs")
    @GetMapping("/filter")
    public ResponseEntity<Page<Job>> getFilteredJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "postedDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String mode,
            @RequestParam(required = false) JobStatus status
    ) {
        Page<Job> jobs = jobService.getFilteredJobs(page, size, sortBy, sortDir, position, company, location, mode, status);
        return ResponseEntity.ok(jobs);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        return jobService.getJobById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new job", description = "Add a new job to the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Job created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        Job createdJob = jobService.createJob(job);
        return ResponseEntity.status(201).body(createdJob);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job job) {
        try {
            return ResponseEntity.ok(jobService.updateJob(id, job));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        try {
            jobService.deleteJob(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}