package com.yigitkagankartal.jobtracker.api;

import com.yigitkagankartal.jobtracker.model.ApplicationStatus;
import com.yigitkagankartal.jobtracker.model.JobApplication;
import com.yigitkagankartal.jobtracker.service.JobApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Job Applications API", description = "Job application management endpoints")
@RestController
@RequestMapping("/api/applications")
public class JobApplicationRestController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationRestController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @Operation(summary = "Get all job applications")
    @GetMapping
    public List<JobApplication> getAll() {
        return jobApplicationService.findAll();
    }

    @Operation(summary = "Get job application by ID")
    @GetMapping("/{id}")
    public ResponseEntity<JobApplication> getById(@PathVariable Long id) {
        return jobApplicationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new job application")
    @PostMapping
    public JobApplication create(@RequestBody JobApplication jobApplication) {
        return jobApplicationService.create(jobApplication);
    }

    @Operation(summary = "Update an existing job application")
    @PutMapping("/{id}")
    public ResponseEntity<JobApplication> update(
            @PathVariable Long id,
            @RequestBody JobApplication updated) {

        try {
            JobApplication saved = jobApplicationService.update(id, updated);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update job application status")
    @PatchMapping("/{id}/status")
    public ResponseEntity<JobApplication> updateStatus(
            @PathVariable Long id,
            @RequestParam ApplicationStatus status) {

        try {
            JobApplication updated = jobApplicationService.updateStatus(id, status);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete job application")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            jobApplicationService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
