package com.example.jobresearch.repositories;

import com.example.jobresearch.domain.models.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}
