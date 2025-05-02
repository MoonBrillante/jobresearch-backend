package com.example.jobresearch.domain.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Job entity representing a job listing.
 * Includes details like position, skills, mode, status, etc.
 */

@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String position;
    private String company;
    private String location;

    //@ElementCollection
    @ElementCollection
    @CollectionTable(name = "job_skills", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "skill")
    private List<String> skills;

    @ElementCollection
    private List<String> tools;

    private String mode; //Remote, Hybrid, Onsite


    private String description;


    private String benefits;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private JobStatus status;

    private String source; // example: Linkedin etc
    @Column(nullable = true)
    private LocalDate postedDate;

    private String notes;

    public Job() {
    }


    public Job(Long id, String position, String company, String location, List<String> skills, List<String> tools, String mode, String description, String benefits, JobStatus status, String source, LocalDate postedDate, String notes) {
        this.id = id;
        this.position = position;
        this.company = company;
        this.location = location;
        this.skills = skills;
        this.tools = tools;
        this.mode = mode;
        this.description = description;
        this.benefits = benefits;
        this.status = status;
        this.source = source;
        this.postedDate = postedDate;
        this.notes = notes;
    }

    public Job(String position, String company, String location, List<String> skills, List<String> tools, String mode, String description, String benefits, JobStatus status, String source, LocalDate postedDate, String notes) {
        this.position = position;
        this.company = company;
        this.location = location;
        this.skills = skills;
        this.tools = tools;
        this.mode = mode;
        this.description = description;
        this.benefits = benefits;
        this.status = status;
        this.source = source;
        this.postedDate = postedDate;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getTools() {
        return tools;
    }

    public void setTools(List<String> tools) {
        this.tools = tools;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
