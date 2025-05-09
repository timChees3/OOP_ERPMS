package com.toinfinityandbeyond.RPMS.controller;

import com.toinfinityandbeyond.RPMS.exception.ResourceNotFoundException;
import com.toinfinityandbeyond.RPMS.model.Appointment;
import com.toinfinityandbeyond.RPMS.model.Doctor;
import com.toinfinityandbeyond.RPMS.model.Feedback;
import com.toinfinityandbeyond.RPMS.model.Patient;
import com.toinfinityandbeyond.RPMS.service.AppointmentService;
import com.toinfinityandbeyond.RPMS.service.DoctorService;
import com.toinfinityandbeyond.RPMS.service.FeedbackService;
import com.toinfinityandbeyond.RPMS.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService,
                              AppointmentService appointmentService,
                              DoctorService doctorService,
                              PatientService patientService) {
        this.feedbackService = feedbackService;
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    /** GET all feedback */
    @GetMapping
    public ResponseEntity<List<Feedback>> getAllFeedback() {
        return ResponseEntity.ok(feedbackService.getAllFeedback());
    }

    /** GET feedback by id */
    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long id) {
        return ResponseEntity.ok(feedbackService.getFeedbackById(id));
    }

    /** GET feedback for a given appointment */
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<Feedback> getFeedbackByAppointment(@PathVariable Long appointmentId) {
        Appointment appt = appointmentService.getAppointmentById(appointmentId);
        Feedback fb = feedbackService.getFeedbackByAppointment(appt);
        if (fb == null) {
            throw new ResourceNotFoundException("Feedback not found for appointment id: " + appointmentId);
        }
        return ResponseEntity.ok(fb);
    }

    /** CHECK if feedback exists for an appointment */
    @GetMapping("/appointment/{appointmentId}/exists")
    public ResponseEntity<Boolean> hasFeedback(@PathVariable Long appointmentId) {
        Appointment appt = appointmentService.getAppointmentById(appointmentId);
        boolean exists = feedbackService.hasFeedback(appt);
        return ResponseEntity.ok(exists);
    }

    /** GET feedback by doctor */
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Feedback>> getFeedbackByDoctor(@PathVariable Long doctorId) {
        Doctor doc = doctorService.getDoctorById(doctorId);
        return ResponseEntity.ok(feedbackService.getFeedbackByDoctor(doc));
    }

    /** GET feedback by patient */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Feedback>> getFeedbackByPatient(@PathVariable Long patientId) {
        Patient p = patientService.getPatientById(patientId);
        return ResponseEntity.ok(feedbackService.getFeedbackByPatient(p));
    }

    /** GET appointments without feedback for a doctor */
    @GetMapping("/doctor/{doctorId}/appointments-without-feedback")
    public ResponseEntity<List<Appointment>> getAppointmentsWithoutFeedback(@PathVariable Long doctorId) {
        Doctor doc = doctorService.getDoctorById(doctorId);
        return ResponseEntity.ok(feedbackService.getAppointmentsWithoutFeedback(doc));
    }

    /** POST create new feedback */
    @PostMapping
    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedback) {
        Feedback created = feedbackService.createFeedback(feedback);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /** PUT update existing feedback */
    @PutMapping("/{id}")
    public ResponseEntity<Feedback> updateFeedback(
            @PathVariable Long id,
            @RequestBody Feedback feedbackDetails
    ) {
        Feedback updated = feedbackService.updateFeedback(id, feedbackDetails);
        return ResponseEntity.ok(updated);
    }

    /** DELETE feedback by id */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }
}
