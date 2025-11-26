package com.yigitkagankartal.jobtracker.controller;
import org.springframework.security.core.Authentication;
import com.yigitkagankartal.jobtracker.model.AppUser;
import com.yigitkagankartal.jobtracker.repository.UserRepository;
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
    private final UserRepository userRepository;

    public JobApplicationController(JobApplicationService jobApplicationService,
                                    UserRepository userRepository) {
        this.jobApplicationService = jobApplicationService;
        this.userRepository = userRepository;
    }



    @GetMapping
    public String listApplications(
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(required = false) String company,
            @RequestParam(defaultValue = "0") int page,
            Model model,
            Authentication authentication
    ) {

        String username = authentication.getName();

        AppUser currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Page<JobApplication> applications = jobApplicationService
                .getApplicationsForUser(currentUser, status, company, page, 5);

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
    public String createApplication(
            @ModelAttribute JobApplication jobApplication,
            Authentication authentication) {

        String username = authentication.getName();

        AppUser currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        jobApplication.setUser(currentUser);

        jobApplicationService.create(jobApplication);
        return "redirect:/applications";
    }

    @GetMapping("/{id}")
    public String viewDetails(@PathVariable Long id,
                              Model model,
                              Authentication authentication) {

        String username = authentication.getName();

        AppUser currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobApplication application = jobApplicationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found: " + id));

        if (!application.getUser().getId().equals(currentUser.getId())) {
            return "redirect:/access-denied";
        }

        model.addAttribute("jobApplication", application);
        return "applications/detail";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id,
                               Model model,
                               Authentication authentication) {

        String username = authentication.getName();
        AppUser currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobApplication application = jobApplicationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found: " + id));

        if (!application.getUser().getId().equals(currentUser.getId())) {
            return "redirect:/access-denied";
        }

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
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam ApplicationStatus status) {

        jobApplicationService.updateStatus(id, status);
        return "redirect:/applications";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         Authentication authentication) {

        String username = authentication.getName();
        AppUser currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobApplication application = jobApplicationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found: " + id));

        if (!application.getUser().getId().equals(currentUser.getId())) {
            return "redirect:/access-denied";
        }

        jobApplicationService.delete(id);
        return "redirect:/applications";
    }
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "errors/access-denied";
    }


}
