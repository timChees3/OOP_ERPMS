package com.toinfinityandbeyond.RPMS.controller;

import com.toinfinityandbeyond.RPMS.exception.BadRequestException;
import com.toinfinityandbeyond.RPMS.exception.ResourceNotFoundException;
import com.toinfinityandbeyond.RPMS.model.Appointment;
import com.toinfinityandbeyond.RPMS.model.Doctor;
import com.toinfinityandbeyond.RPMS.model.Feedback;
import com.toinfinityandbeyond.RPMS.model.Medication;
import com.toinfinityandbeyond.RPMS.model.Patient;
import com.toinfinityandbeyond.RPMS.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private  DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    /** GET /api/doctors */
    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    /** GET /api/doctors/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Doctor d = doctorService.getDoctorById(id);
        return ResponseEntity.ok(d);
    }

    /** POST /api/doctors */
    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        Doctor created = doctorService.createDoctor(doctor);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /** PUT /api/doctors/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(
            @PathVariable Long id,
            @RequestBody Doctor doctorDetails
    ) {
        Doctor updated = doctorService.updateDoctor(id, doctorDetails);
        return ResponseEntity.ok(updated);
    }

    /** DELETE /api/doctors/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    /** GET /api/doctors/{id}/appointments */
    @GetMapping("/{id}/appointments")
    public ResponseEntity<List<Appointment>> getDoctorAppointments(@PathVariable("id") Long doctorId) {
        List<Appointment> list = doctorService.getDoctorAppointments(doctorId);
        return ResponseEntity.ok(list);
    }

    /**
     * PUT /api/doctors/{doctorId}/appointments/{appointmentId}/status
     *   ?status=APPROVED
     */
    @PutMapping("/{doctorId}/appointments/{appointmentId}/status")
    public ResponseEntity<Appointment> updateAppointmentStatus(
            @PathVariable Long doctorId,
            @PathVariable Long appointmentId,
            @RequestParam("status") Appointment.Status status
    ) {
        Appointment updated = doctorService.updateAppointmentStatus(doctorId, appointmentId, status);
        return ResponseEntity.ok(updated);
    }

    /** POST /api/doctors/{doctorId}/appointments/{appointmentId}/medications */
    @PostMapping("/{doctorId}/appointments/{appointmentId}/medications")
    public ResponseEntity<Medication> prescribeMedication(
            @PathVariable Long doctorId,
            @PathVariable Long appointmentId,
            @RequestBody Medication medication
    ) {
        Medication created = doctorService.prescribeMedication(doctorId, appointmentId, medication);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /** POST /api/doctors/{doctorId}/appointments/{appointmentId}/feedback */
    @PostMapping("/{doctorId}/appointments/{appointmentId}/feedback")
    public ResponseEntity<Feedback> provideFeedback(
            @PathVariable Long doctorId,
            @PathVariable Long appointmentId,
            @RequestBody Feedback feedback
    ) {
        Feedback created = doctorService.provideFeedback(doctorId, appointmentId, feedback);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /** GET /api/doctors/{id}/patients */
    @GetMapping("/{id}/patients")
    public ResponseEntity<List<Patient>> getDoctorPatients(@PathVariable("id") Long doctorId) {
        List<Patient> list = doctorService.getDoctorPatients(doctorId);
        return ResponseEntity.ok(list);
    }

    /** GET /api/doctors/specialization/{specialization} */
    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<Doctor>> getDoctorsBySpecialization(
            @PathVariable String specialization
    ) {
        List<Doctor> list = doctorService.getDoctorsBySpecialization(specialization);
        return ResponseEntity.ok(list);
    }
}
