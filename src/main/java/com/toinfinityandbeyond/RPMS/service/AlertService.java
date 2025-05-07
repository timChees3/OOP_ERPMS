package com.toinfinityandbeyond.RPMS.service;

import com.toinfinityandbeyond.RPMS.model.Doctor;
import com.toinfinityandbeyond.RPMS.model.Patient;
import com.toinfinityandbeyond.RPMS.model.Vitals;
import com.toinfinityandbeyond.RPMS.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AlertService {

    @Autowired
    private  EmailService emailService;

    @Autowired
    private  DoctorRepository doctorRepository;

    @Async
    public void sendVitalsAlert(Vitals vitals) {
        if (!vitals.isCritical()) {
            return;
        }

        Patient patient = vitals.getPatient();

        // Get all doctors who are available for emergency
        List<Doctor> availableDoctors = doctorRepository.findByAvailableForEmergencyTrue();

        // Create alert message
        String alertSubject = "EMERGENCY ALERT: Critical Vitals Detected for Patient " +
                patient.getFirstName() + " " + patient.getLastName();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String recordedTime = vitals.getRecordedAt().format(formatter);

        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("CRITICAL VITALS ALERT\n\n");
        messageBuilder.append("Patient: ").append(patient.getFirstName()).append(" ").append(patient.getLastName()).append("\n");
        messageBuilder.append("Patient ID: ").append(patient.getId()).append("\n");
        messageBuilder.append("Recorded at: ").append(recordedTime).append("\n\n");
        messageBuilder.append("CRITICAL VITALS:\n");
        messageBuilder.append(vitals.getCriticalNotes()).append("\n\n");

        // Add all vitals data
        messageBuilder.append("Complete Vitals Information:\n");
        if (vitals.getHeartRate() != null) {
            messageBuilder.append("Heart Rate: ").append(vitals.getHeartRate()).append(" bpm\n");
        }
        if (vitals.getOxygenLevel() != null) {
            messageBuilder.append("Oxygen Level: ").append(vitals.getOxygenLevel()).append("%\n");
        }
        if (vitals.getTemperature() != null) {
            messageBuilder.append("Temperature: ").append(vitals.getTemperature()).append("°C\n");
        }
        if (vitals.getSystolicPressure() != null && vitals.getDiastolicPressure() != null) {
            messageBuilder.append("Blood Pressure: ").append(vitals.getSystolicPressure())
                    .append("/").append(vitals.getDiastolicPressure()).append(" mmHg\n");
        }
        if (vitals.getRespiratoryRate() != null) {
            messageBuilder.append("Respiratory Rate: ").append(vitals.getRespiratoryRate()).append(" breaths/min\n");
        }
        if (vitals.getBloodSugar() != null) {
            messageBuilder.append("Blood Sugar: ").append(vitals.getBloodSugar()).append(" mg/dL\n");
        }

        messageBuilder.append("\nPlease take immediate action!\n");
        messageBuilder.append("Open the Remote Patient Monitoring System to view detailed information.");

        String alertMessage = messageBuilder.toString();

        // Send alert to all available doctors
        for (Doctor doctor : availableDoctors) {
            emailService.sendEmail(doctor.getEmail(), alertSubject, alertMessage);
        }

        // Send alert to patient's emergency contact if available
        if (patient.getEmergencyContact() != null && !patient.getEmergencyContact().isEmpty()) {
            emailService.sendEmail(patient.getEmergencyContact(), alertSubject, alertMessage);
        }
    }

    @Async
    public void sendAppointmentAlert(String to, String subject, String message) {
        emailService.sendEmail(to, subject, message);
    }

    @Async
    public void sendMedicationReminderAlert(String to, String subject, String message) {
        emailService.sendEmail(to, subject, message);
    }
}