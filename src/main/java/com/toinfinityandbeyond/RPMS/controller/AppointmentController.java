package com.toinfinityandbeyond.RPMS.controller;

import com.toinfinityandbeyond.RPMS.exception.ResourceNotFoundException;
import com.toinfinityandbeyond.RPMS.model.Appointment;
import com.toinfinityandbeyond.RPMS.model.Doctor;
import com.toinfinityandbeyond.RPMS.model.Patient;
import com.toinfinityandbeyond.RPMS.service.AppointmentService;
import com.toinfinityandbeyond.RPMS.service.DoctorService;
import com.toinfinityandbeyond.RPMS.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final DoctorService doctorService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService,
                                 PatientService patientService,
                                 DoctorService doctorService) {
        this.appointmentService = appointmentService;
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    /**
     * GET /api/appointments
     */
    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    /**
     * GET /api/appointments/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        Appointment appt = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appt);
    }

    /**
     * GET /api/appointments/patient/{patientId}
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByPatient(@PathVariable Long patientId) {
        Patient patient = patientService.getPatientById(patientId);
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patient));
    }

    /**
     * GET /api/appointments/doctor/{doctorId}
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByDoctor(@PathVariable Long doctorId) {
        Doctor doctor = doctorService.getDoctorById(doctorId);
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctor));
    }

    /**
     * GET /api/appointments/status?status=...
     */
    @GetMapping("/status")
    public ResponseEntity<List<Appointment>> getAppointmentsByStatus(
            @RequestParam("status") Appointment.Status status) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByStatus(status));
    }

    /**
     * GET /api/appointments/patient/{patientId}/upcoming
     */
    @GetMapping("/patient/{patientId}/upcoming")
    public ResponseEntity<List<Appointment>> getUpcomingByPatient(@PathVariable Long patientId) {
        Patient patient = patientService.getPatientById(patientId);
        return ResponseEntity.ok(appointmentService.getUpcomingAppointmentsByPatient(patient));
    }

    /**
     * GET /api/appointments/doctor/{doctorId}/upcoming
     */
    @GetMapping("/doctor/{doctorId}/upcoming")
    public ResponseEntity<List<Appointment>> getUpcomingByDoctor(@PathVariable Long doctorId) {
        Doctor doctor = doctorService.getDoctorById(doctorId);
        return ResponseEntity.ok(appointmentService.getUpcomingAppointmentsByDoctor(doctor));
    }

    /**
     * GET /api/appointments/today
     */
    @GetMapping("/today")
    public ResponseEntity<List<Appointment>> getTodayAppointments() {
        return ResponseEntity.ok(appointmentService.getTodayAppointments());
    }

    /**
     * POST /api/appointments
     */
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        Appointment created = appointmentService.createAppointment(appointment);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * PUT /api/appointments/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(
            @PathVariable Long id,
            @RequestBody Appointment appointmentDetails) {
        Appointment updated = appointmentService.updateAppointment(id, appointmentDetails);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/appointments/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/appointments/doctor/{doctorId}/available
     * Checks if a doctor is available at a given date-time
     */
    @GetMapping("/doctor/{doctorId}/available")
    public ResponseEntity<Boolean> isDoctorAvailable(
            @PathVariable Long doctorId,
            @RequestParam("scheduledAt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime scheduledAt) {
        Doctor doctor = doctorService.getDoctorById(doctorId);
        boolean available = appointmentService.isDoctorAvailable(doctor, scheduledAt);
        return ResponseEntity.ok(available);
    }
}