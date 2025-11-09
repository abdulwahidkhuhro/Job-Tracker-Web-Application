// package com.spring.boot.job.tracker.app.schedular;


// import com.spring.boot.job.tracker.app.service.EmailService;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Component;

// @Component
// public class FollowUpScheduler {

//     private final EmailService emailService;

//     public FollowUpScheduler(EmailService emailService) {
//         this.emailService = emailService;
//     }

//     // Runs every day at 9 AM
//     @Scheduled(cron = "0 0 9 * * ?")
//     public void sendFollowUpReminders() {
//         // For now, just simulate
//         System.out.println(" Checking for follow-up reminders...");

//         // Example email (you will later fetch data from DB)
//         String to = "user@example.com";
//         String subject = "Job Application Follow-Up Reminder";
//         String text = "Hi! Don’t forget to follow up on your Google Backend Engineer application today.";
//         emailService.sendEmail(to, subject, text);

//         System.out.println("Reminder email sent successfully.");
//     }
// }

