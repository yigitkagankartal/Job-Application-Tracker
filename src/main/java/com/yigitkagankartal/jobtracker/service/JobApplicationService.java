package com.yigitkagankartal.jobtracker.service;

import com.yigitkagankartal.jobtracker.model.ApplicationStatus;
import com.yigitkagankartal.jobtracker.model.JobApplication;
import com.yigitkagankartal.jobtracker.repository.JobApplicationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.yigitkagankartal.jobtracker.model.AppUser;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;


@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;

    public JobApplicationService(JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
    }

    public Page<JobApplication> getApplications(ApplicationStatus status,
                                                String companyName,
                                                int page,
                                                int size) {

        Pageable pageable = PageRequest.of(page, size);

        if (status != null && companyName != null && !companyName.isBlank()) {
            return jobApplicationRepository
                    .findByStatusAndCompanyNameContainingIgnoreCase(status, companyName, pageable);
        } else if (status != null) {
            return jobApplicationRepository.findByStatus(status, pageable);
        } else if (companyName != null && !companyName.isBlank()) {
            return jobApplicationRepository.findByCompanyNameContainingIgnoreCase(companyName, pageable);
        } else {
            return jobApplicationRepository.findAll(pageable);
        }
    }

    public JobApplication create(JobApplication jobApplication) {
        if (jobApplication.getApplicationDate() == null) {
            jobApplication.setApplicationDate(LocalDate.now());
        }
        if (jobApplication.getStatus() == null) {
            jobApplication.setStatus(ApplicationStatus.APPLIED);
        }
        return jobApplicationRepository.save(jobApplication);
    }

    public Optional<JobApplication> findById(Long id) {
        return jobApplicationRepository.findById(id);
    }

    public JobApplication update(Long id, JobApplication updated) {
        JobApplication existing = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job application not found: " + id));

        // Sadece formdan gelen alanları güncelliyoruz
        existing.setCompanyName(updated.getCompanyName());
        existing.setPosition(updated.getPosition());
        existing.setStatus(updated.getStatus());
        existing.setNotes(updated.getNotes());

        return jobApplicationRepository.save(existing);
    }

    public void delete(Long id) {
        jobApplicationRepository.deleteById(id);
    }

    public JobApplication updateStatus(Long id, ApplicationStatus status) {
        JobApplication job = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job application not found: " + id));

        job.setStatus(status);
        return jobApplicationRepository.save(job);
    }

    public List<JobApplication> findAll() {
        return jobApplicationRepository.findAll();
    }

    public Page<JobApplication> getApplicationsForUser(
            AppUser user,
            ApplicationStatus status,
            String companyName,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);

        if (status != null && companyName != null && !companyName.isBlank()) {
            return jobApplicationRepository
                    .findByUserAndStatusAndCompanyNameContainingIgnoreCase(user, status, companyName, pageable);
        } else if (status != null) {
            return jobApplicationRepository.findByUserAndStatus(user, status, pageable);
        } else if (companyName != null && !companyName.isBlank()) {
            return jobApplicationRepository.findByUserAndCompanyNameContainingIgnoreCase(user, companyName, pageable);
        } else {
            return jobApplicationRepository.findByUser(user, pageable);
        }
    }



}
