package com.yigitkagankartal.jobtracker.controller;

import com.yigitkagankartal.jobtracker.model.ApplicationStatus;
import com.yigitkagankartal.jobtracker.model.JobApplication;
import com.yigitkagankartal.jobtracker.service.JobApplicationService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @GetMapping
    public String listApplications(
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(required = false) String company,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        Page<JobApplication> applications = jobApplicationService
                .getApplications(status, company, page, 5);

        model.addAttribute("applications", applications);
        model.addAttribute("statusValues", ApplicationStatus.values());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("company", company);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", applications.getTotalPages());

        return "applications/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("jobApplication", new JobApplication());
        model.addAttribute("statusValues", ApplicationStatus.values());
        return "applications/form";
    }

    @PostMapping("/create")
    public String createApplication(@ModelAttribute JobApplication jobApplication) {
        jobApplicationService.create(jobApplication);
        return "redirect:/applications";
    }

    @GetMapping("/{id}")
    public String viewDetails(@PathVariable Long id, Model model) {
        JobApplication application = jobApplicationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found: " + id));

        model.addAttribute("jobApplication", application);
        return "applications/detail";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        JobApplication application = jobApplicationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found: " + id));

        model.addAttribute("jobApplication", application);
        model.addAttribute("statusValues", ApplicationStatus.values());
        return "applications/form";
    }
    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @ModelAttribute JobApplication jobApplication) {
        jobApplicationService.update(id, jobApplication);
        return "redirect:/applications";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam ApplicationStatus status) {
        jobApplicationService.updateStatus(id, status);
        return "redirect:/applications";
    }

    // SİL
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        jobApplicationService.delete(id);
        return "redirect:/applications";
    }
}
