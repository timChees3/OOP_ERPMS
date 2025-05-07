package com.toinfinityandbeyond.RPMS.service;


import com.rpms.entity.Appointment;
import com.rpms.entity.Doctor;
import com.rpms.entity.Feedback;
import com.rpms.entity.Medication;
import com.rpms.entity.Patient;
import com.rpms.entity.Report;
import com.rpms.entity.User;
import com.rpms.entity.Vitals;
import com.rpms.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private EmailUtil emailUtil;

    @Async
    public void sendWelcomeEmail(User user) {
        try {
            // Create model for template
            Map<String, Object> model = new HashMap<>();
            model.put("name", user.getName());
            model.put("username", user.getUsername());

            // Send email
            sendEmail(
                    user.getEmail(),
                    "Welcome to Remote Patient Monitoring System",
                    "welcome-email",
                    model
            );
        } catch (Exception e) {
            // Log error but don't throw exception to prevent transaction rollback
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }
    }

    @Async
    public void sendAppointmentRequestNotificationToDoctor(Appointment appointment) {
        try {
            Doctor doctor = appointment.getDoctor();
            Patient patient = appointment.getPatient();

            // Create model for template
            Map<String, Object> model = new HashMap<>();
            model.put("doctorName", doctor.getName());
            model.put("patientName", patient.getName());
            model.put("appointmentDate", emailUtil.formatDateTime(appointment.getAppointmentDateTime()));
            model.put("appointmentType", appointment.getConsultationType());
            model.put("reason", appointment.getReason());

            // Send email
            sendEmail(
                    doctor.getEmail(),
                    "New Appointment Request",
                    "appointment-request-doctor",
                    model
            );
        } catch (Exception e) {
            System.err.println("Failed to send appointment notification to doctor: " + e.getMessage());
        }
    }

    @Async
    public void sendAppointmentRequestConfirmationToPatient(Appointment appointment) {
        try {
            Doctor doctor = appointment.getDoctor();
            Patient patient = appointment.getPatient();

            // Create model for template
            Map<String, Object> model = new HashMap<>();
            model.put("patientName", patient.getName());
            model.put("doctorName", doctor.getName());
            model.put("appointmentDate", emailUtil.formatDateTime(appointment.getAppointmentDateTime()));
            model.put("appointmentType", appointment.getConsultationType());

            // Send email
            sendEmail(
                    patient.getEmail(),
                    "Appointment Request Confirmation",
                    "appointment-request-patient",
                    model
            );
        } catch (Exception e) {
            System.err.println("Failed to send appointment confirmation to patient: " + e.getMessage());
        }
    }

    @Async
    public void sendAppointmentStatusUpdateEmail(Appointment appointment) {
        try {
            Doctor doctor = appointment.getDoctor();
            Patient patient = appointment.getPatient();

            // Create model for template
            Map<String, Object> model = new HashMap<>();
            model.put("patientName", patient.getName());
            model.put("doctorName", doctor.getName());
            model.put("appointmentDate", emailUtil.formatDateTime(appointment.getAppointmentDateTime()));
            model.put("appointmentType", appointment.getConsultationType());
            model.put("status", appointment.getStatus());
            model.put("reason", appointment.getNotes());

            // Send email
            sendEmail(
                    patient.getEmail(),
                    "Appointment Status Update",
                    "appointment-status-update",
                    model
            );
        } catch (Exception e) {
            System.err.println("Failed to send appointment status update: " + e.getMessage());
        }
    }

    @Async
    public void sendVideoConsultationLinkEmail(Appointment appointment) {
        try {
            Doctor doctor = appointment.getDoctor();
            Patient patient = appointment.getPatient();

            // Create model for template
            Map<String, Object> model = new HashMap<>();
            model.put("name", patient.getName());
            model.put("doctorName", doctor.getName());
            model.put("appointmentDate", emailUtil.formatDateTime(appointment.getAppointmentDateTime()));
            model.put("meetingLink", appointment.getMeetingLink());

            // Send email to patient
            sendEmail(
                    patient.getEmail(),
                    "Video Consultation Link",
                    "video-consultation-link",
                    model
            );

            // Update model for doctor
            model.put("name", doctor.getName());
            model.put("patientName", patient.getName());

            // Send email to doctor
            sendEmail(
                    doctor.getEmail(),
                    "Video Consultation Link",
                    "video-consultation-link",
                    model
            );
        } catch (Exception e) {
            System.err.println("Failed to send video consultation link: " + e.getMessage());
        }
    }

    @Async
    public void sendPrescriptionNotificationEmail(Patient patient, Medication medication) {
        try {
            // Create model for template
            Map<String, Object> model = new HashMap<>();
            model.put("patientName", patient.getName());
            model.put("medicationName", medication.getName());
            model.put("dosage", medication.getDosage());
            model.put("frequency", medication.getFrequency());
            model.put("startDate", emailUtil.formatDate(medication.getStartDate()));
            model.put("endDate", emailUtil.formatDate(medication.getEndDate()));
            model.put("instructions", medication.getInstructions());

            // Send email
            sendEmail(
                    patient.getEmail(),
                    "New Medication Prescribed",
                    "medication-prescribed",
                    model
            );
        } catch (Exception e) {
            System.err.println("Failed to send prescription notification: " + e.getMessage());
        }
    }

    @Async
    public void sendFeedbackNotificationEmail(Patient patient, Feedback feedback) {
        try {
            // Create model for template
            Map<String, Object> model = new HashMap<>();
            model.put("patientName", patient.getName());
            model.put("doctorName", feedback.getAppointment().getDoctor().getName());
            model.put("diagnosis", feedback.getDiagnosis());
            model.put("observations", feedback.getObservations());
            model.put("recommendations", feedback.getRecommendations());
            model.put("followUpRequired", feedback.isFollowUpRequired());
            if (feedback.isFollowUpRequired()) {
                model.put("followUpDate", emailUtil.formatDate(feedback.getFollowUpDate()));
            }

            // Send email
            sendEmail(
                    patient.getEmail(),
                    "Appointment Feedback",
                    "feedback-notification",
                    model
            );
        } catch (Exception e) {
            System.err.println("Failed to send feedback notification: " + e.getMessage());
        }
    }

    @Async
    public void sendReportGeneratedEmail(Report report) {
        try {
            Patient patient = report.getPatient();
            Doctor doctor = report.getDoctor();

            // Create model for template
            Map<String, Object> model = new HashMap<>();
            model.put("patientName", patient.getName());
            model.put("doctorName", doctor.getName());
            model.put("reportDate", emailUtil.formatDate(report.getGeneratedDate()));
            model.put("diagnosis", report.getFeedback().getDiagnosis());

            // Send email with attachment
            sendEmailWithAttachment(
                    patient.getEmail(),
                    "Medical Report",
                    "report-generated",
                    model,
                    "medical-report.pdf",
                    report.getPdfContent()
            );
        } catch (Exception e) {
            System.err.println("Failed to send report generated email: " + e.getMessage());
        }
    }

    @Async
    public void sendEmergencyAlertEmail(Doctor doctor, Patient patient, Vitals vitals) {
        try {
            // Create model for template
            Map<String, Object> model = new HashMap<>();
            model.put("doctorName", doctor.getName());
            model.put("patientName", patient.getName());
            model.put("patientId", patient.getId());
            model.put("contactNumber", patient.getPhone());
            model.put("timestamp", emailUtil.formatDateTime(vitals.getTimestamp()));
            model.put("heartRate", vitals.getHeartRate());
            model.put("bloodPressure", vitals.getBloodPressureSystolic() + "/" + vitals.getBloodPressureDiastolic());
            model.put("temperature", vitals.getTemperature());
            model.put("respiratoryRate", vitals.getRespiratoryRate());
            model.put("oxygenSaturation", vitals.getOxygenSaturation());

            // Send email with high priority
            sendUrgentEmail(
                    doctor.getEmail(),
                    "EMERGENCY ALERT: Abnormal Vitals Detected",
                    "emergency-alert",
                    model
            );
        } catch (Exception e) {
            System.err.println("Failed to send emergency alert: " + e.getMessage());
        }
    }

    @Async
    public void sendMedicationReminderEmail(Patient patient, Medication medication) {
        try {
            // Create model for template
            Map<String, Object> model = new HashMap<>();
            model.put("patientName", patient.getName());
            model.put("medicationName", medication.getName());
            model.put("endDate", emailUtil.formatDate(medication.getEndDate()));

            // Send email
            sendEmail(
                    patient.getEmail(),
                    "Medication Reminder",
                    "medication-reminder",
                    model
            );
        } catch (Exception e) {
            System.err.println("Failed to send medication reminder: " + e.getMessage());
        }
    }

    @Async
    public void sendPasswordResetEmail(User user, String resetToken) {
        try {
            // Create model for template
            Map<String, Object> model = new HashMap<>();
            model.put("name", user.getName());
            model.put("resetToken", resetToken);
            model.put("expiryTime", "24 hours");

            // Send email
            sendEmail(
                    user.getEmail(),
                    "Password Reset Request",
                    "password-reset",
                    model
            );
        } catch (Exception e) {
            System.err.println("Failed to send password reset email: " + e.getMessage());
        }
    }

    private void sendEmail(String to, String subject, String templateName, Map<String, Object> model)
            throws MessagingException {
        // Process template
        Context context = new Context();
        model.forEach(context::setVariable);
        String htmlContent = templateEngine.process(templateName, context);

        // Create and send email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    private void sendEmailWithAttachment(String to, String subject, String templateName,
                                         Map<String, Object> model, String attachmentName, byte[] attachment) throws MessagingException {
        // Process template
        Context context = new Context();
        model.forEach(context::setVariable);
        String htmlContent = templateEngine.process(templateName, context);

        // Create and send email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.addAttachment(attachmentName, () -> new java.io.ByteArrayInputStream(attachment));

        mailSender.send(message);
    }

    private void sendUrgentEmail(String to, String subject, String templateName, Map<String, Object> model)
            throws MessagingException {
        // Process template
        Context context = new Context();
        model.forEach(context::setVariable);
        String htmlContent = templateEngine.process(templateName, context);

        // Create and send urgent email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        // Set high priority headers
        message.addHeader("X-Priority", "1");
        message.addHeader("Importance", "High");
        message.addHeader("X-MSMail-Priority", "High");

        mailSender.send(message);
    }
}
