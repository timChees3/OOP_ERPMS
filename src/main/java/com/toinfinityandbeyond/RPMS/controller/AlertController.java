package com.toinfinityandbeyond.RPMS.controller;

import com.toinfinityandbeyond.RPMS.exception.ResourceNotFoundException;
import com.toinfinityandbeyond.RPMS.model.Appointment;
import com.toinfinityandbeyond.RPMS.model.Patient;
import com.toinfinityandbeyond.RPMS.model.Vitals;
import com.toinfinityandbeyond.RPMS.repository.AppointmentRepository;
import com.toinfinityandbeyond.RPMS.repository.PatientRepository;
import com.toinfinityandbeyond.RPMS.repository.VitalsRepository;
import com.toinfinityandbeyond.RPMS.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    @Autowired
    private AlertService alertService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private VitalsRepository vitalsRepository;

    @Autowired
    private PatientRepository patientRepository;

    /**
     * POST /api/alerts/appointment/{appointmentId}/status
     * Manually trigger an appointment‐status update email.
     */
    @PostMapping("/appointment/{appointmentId}/status")
    public ResponseEntity<Void> triggerAppointmentStatusUpdate(@PathVariable Long appointmentId)
    {
        Appointment appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));
        alertService.sendAppointmentStatusUpdate(appt);
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/alerts/vitals/{vitalsId}/critical
     * Manually trigger a critical‐vitals email for a specific Vitals record.
     */
    @PostMapping("/vitals/{vitalsId}/critical")
    public ResponseEntity<Void> triggerCriticalVitalsAlert(@PathVariable Long vitalsId)
    {
        Vitals v = vitalsRepository.findById(vitalsId)
                .orElseThrow(() -> new ResourceNotFoundException("Vitals not found with id: " + vitalsId));
        alertService.sendCriticalVitalsAlerts(v);
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/alerts/panic/{patientId}
     * Trigger the panic‐button alert for a given patient.
     */
    @PostMapping("/panic/{patientId}")
    public ResponseEntity<Void> triggerPanicButton(@PathVariable Long patientId)
    {
        Patient p = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        alertService.panicButtonPressed(p);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping("/medications/reminders")
    public ResponseEntity<Void> triggerMedicationReminders()
    {
        alertService.sendDailyMedicationReminders();
        return ResponseEntity.accepted().build();
    }
}
