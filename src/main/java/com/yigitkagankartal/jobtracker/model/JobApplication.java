package com.yigitkagankartal.jobtracker.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import com.yigitkagankartal.jobtracker.model.AppUser;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(
        name = "job_applications",
        indexes = {
                @Index(name = "idx_job_status", columnList = "status"),
                @Index(name = "idx_job_company_name", columnList = "company_name"),
                @Index(name = "idx_job_application_date", columnList = "application_date")
        }
)
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String position;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    @Column(name = "application_date", nullable = false)
    private LocalDate applicationDate;

    @Column(columnDefinition = "text")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;


    public JobApplication() {
    }

    public JobApplication(Long id,
                          String companyName,
                          String position,
                          ApplicationStatus status,
                          LocalDate applicationDate,
                          String notes) {
        this.id = id;
        this.companyName = companyName;
        this.position = position;
        this.status = status;
        this.applicationDate = applicationDate;
        this.notes = notes;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

}
