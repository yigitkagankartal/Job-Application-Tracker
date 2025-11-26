package com.yigitkagankartal.jobtracker.repository;
import com.yigitkagankartal.jobtracker.model.AppUser;

import com.yigitkagankartal.jobtracker.model.ApplicationStatus;
import com.yigitkagankartal.jobtracker.model.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    Page<JobApplication> findByUser(AppUser user, Pageable pageable);

    Page<JobApplication> findByUserAndStatus(
            AppUser user,
            ApplicationStatus status,
            Pageable pageable);

    Page<JobApplication> findByUserAndCompanyNameContainingIgnoreCase(
            AppUser user,
            String companyName,
            Pageable pageable);

    Page<JobApplication> findByUserAndStatusAndCompanyNameContainingIgnoreCase(
            AppUser user,
            ApplicationStatus status,
            String companyName,
            Pageable pageable);

    Page<JobApplication> findByStatus(ApplicationStatus status, Pageable pageable);

    Page<JobApplication> findByCompanyNameContainingIgnoreCase(String companyName, Pageable pageable);

    Page<JobApplication> findByStatusAndCompanyNameContainingIgnoreCase(
            ApplicationStatus status,
            String companyName,
            Pageable pageable
    );
}
