package com.example.jobresearch.domain.services;

import com.example.jobresearch.domain.models.Job;
import com.example.jobresearch.repositories.JobRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class JobService {
    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    // get all Job
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    // Add a new Job
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
                    existingJob.setPostedDate(updatedJob.getPostedDate());
                    existingJob.setNotes(updatedJob.getNotes());
                    return jobRepository.save(existingJob);
                })
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));

    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

}


