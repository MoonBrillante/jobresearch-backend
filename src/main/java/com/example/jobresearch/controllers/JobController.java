package com.example.jobresearch.controllers;

import com.example.jobresearch.domain.models.Job;
import com.example.jobresearch.domain.services.JobService;
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

    // get paginated jobs with sorting
    @Operation(
            summary = "Get paginated jobs with sorting",
            description = "Returns paginated job listings for the JobList page."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved paginated jobs")
    @GetMapping("/page")
    public ResponseEntity<Page<Job>> getPaginatedJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "postedDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ){
        Page<Job> jobs = jobService.getPaginatedJobs(page, size, sortBy, sortDir);
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