package com.toinfinityandbeyond.RPMS.controller;

import com.toinfinityandbeyond.RPMS.exception.BadRequestException;
import com.toinfinityandbeyond.RPMS.exception.ResourceNotFoundException;
import com.toinfinityandbeyond.RPMS.model.Appointment;
import com.toinfinityandbeyond.RPMS.model.Patient;
import com.toinfinityandbeyond.RPMS.model.Report;
import com.toinfinityandbeyond.RPMS.model.Vitals;
import com.toinfinityandbeyond.RPMS.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private  PatientService patientService;

    /** GET /api/patients */
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    /** GET /api/patients/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id);
        return ResponseEntity.ok(patient);
    }

    /** POST /api/patients */
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient created = patientService.createPatient(patient);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /** PUT /api/patients/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable Long id,
            @RequestBody Patient patientDetails
    ) {
        Patient updated = patientService.updatePatient(id, patientDetails);
        return ResponseEntity.ok(updated);
    }

    /** DELETE /api/patients/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    /** POST /api/patients/{id}/appointments */
    @PostMapping("/{id}/appointments")
    public ResponseEntity<Appointment> requestAppointment(
            @PathVariable("id") Long patientId,
            @RequestBody Appointment appointment
    ) {
        Appointment created = patientService.requestAppointment(patientId, appointment);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /** GET /api/patients/{id}/appointments */
    @GetMapping("/{id}/appointments")
    public ResponseEntity<List<Appointment>> getPatientAppointments(@PathVariable("id") Long patientId) {
        List<Appointment> list = patientService.getPatientAppointments(patientId);
        return ResponseEntity.ok(list);
    }

    /** POST /api/patients/{id}/vitals */
    @PostMapping("/{id}/vitals")
    public ResponseEntity<Vitals> addVitals(
            @PathVariable("id") Long patientId,
            @RequestBody Vitals vitals
    ) {
        Vitals created = patientService.addVitals(patientId, vitals);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /** GET /api/patients/{id}/vitals */
    @GetMapping("/{id}/vitals")
    public ResponseEntity<List<Vitals>> getPatientVitals(@PathVariable("id") Long patientId) {
        List<Vitals> list = patientService.getPatientVitals(patientId);
        return ResponseEntity.ok(list);
    }

    /** GET /api/patients/{id}/reports */
    @GetMapping("/{id}/reports")
    public ResponseEntity<List<Report>> getPatientReport(@PathVariable("id") Long patientId) {
        List<Report> list = patientService.getPatientReports(patientId);
        return ResponseEntity.ok(list);
    }

    /** GET /api/patients/doctor/{doctorId} */
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Patient>> getPatientsByDoctor(@PathVariable Long doctorId) {
        List<Patient> list = patientService.getPatientsByDoctor(doctorId);
        return ResponseEntity.ok(list);
    }
}
