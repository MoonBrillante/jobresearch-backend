package com.example.jobresearch.controllers;


import com.example.jobresearch.domain.models.Job;
import com.example.jobresearch.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/import")
public class JobImportController {

    @Autowired
    private JobRepository jobRepository;

    @PostMapping
    public ResponseEntity<String> importJobs(@RequestBody List<Job> jobs) {
        jobRepository.saveAll(jobs);
        return ResponseEntity.ok("Import successful，total " + jobs.size() + " records");
    }
}