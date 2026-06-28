package com.example.jobresearch.domain.services;

import com.example.jobresearch.domain.models.Job;
import com.example.jobresearch.repositories.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.example.jobresearch.domain.models.JobStatus;
import com.example.jobresearch.repositories.JobSpecification;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    public Job updateJob(Long id, Job updatedJob){
        return jobRepository.findById(id)
                .map(existingJob->{
                    existingJob.setPosition(updatedJob.getPosition());
                    existingJob.setCompany(updatedJob.getCompany());
                    existingJob.setLocation(updatedJob.getLocation());
                    existingJob.setSkills(updatedJob.getSkills());
                    existingJob.setTools(updatedJob.getTools());
                    existingJob.setMode(updatedJob.getMode());
                    existingJob.setDescription(updatedJob.getDescription());
                    existingJob.setBenefits(updatedJob.getBenefits());
                    existingJob.setStatus(updatedJob.getStatus());
                    existingJob.setSource(updatedJob.getSource());
                    existingJob.setUrl(updatedJob.getUrl());
                    existingJob.setSalary(updatedJob.getSalary());
                    existingJob.setExternalJobId(updatedJob.getExternalJobId());
                    existingJob.setScrapedFrom(updatedJob.getScrapedFrom());
                    existingJob.setPostedDate(updatedJob.getPostedDate());
                    existingJob.setNotes(updatedJob.getNotes());
                    return jobRepository.save(existingJob);
                })
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
    }


    private String validateSortBy(String sortBy) {
        List<String> allowedFields = List.of(
                "postedDate",
                "position",
                "company",
                "location",
                "status",
                "source"
        );

        if (allowedFields.contains(sortBy)) {
            return sortBy;
        }

        return "postedDate";
    }

    public Page<Job> getFilteredJobs(
            int page, int size, String sortBy, String sortDir,
            String position, String company, String location,
            String mode, JobStatus status
    ) {
        String validatedSortBy = validateSortBy(sortBy);

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(validatedSortBy).ascending()
                : Sort.by(validatedSortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Job> spec = JobSpecification.filterBy(position, company, location, mode, status);

        return jobRepository.findAll(spec, pageable);
    }


    public void deleteJob(Long id) {
        if (!jobRepository.existsById(id)){
            throw new RuntimeException("Job not found with id: " + id);
        }

        jobRepository.deleteById(id);
    }
}


