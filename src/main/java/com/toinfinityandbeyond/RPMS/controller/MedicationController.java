package com.toinfinityandbeyond.RPMS.controller;

import com.toinfinityandbeyond.RPMS.model.Appointment;
import com.toinfinityandbeyond.RPMS.model.Medication;
import com.toinfinityandbeyond.RPMS.model.Patient;
import com.toinfinityandbeyond.RPMS.service.AppointmentService;
import com.toinfinityandbeyond.RPMS.service.MedicationService;
import com.toinfinityandbeyond.RPMS.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medications")
public class MedicationController {

    @Autowired
    private MedicationService medicationService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;

    /** GET /api/medications */
    @GetMapping
    public ResponseEntity<List<Medication>> getAllMedications() {
        List<Medication> meds = medicationService.getAllMedications();
        return ResponseEntity.ok(meds);
    }

    /** GET /api/medications/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<Medication> getMedicationById(@PathVariable Long id) {
        Medication med = medicationService.getMedicationById(id);
        return ResponseEntity.ok(med);
    }

    /** GET /api/medications/appointment/{appointmentId} */
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<Medication>> getMedicationsByAppointment(@PathVariable Long appointmentId) {
        Appointment appt = appointmentService.getAppointmentById(appointmentId);
        List<Medication> meds = medicationService.getMedicationsByAppointment(appt);
        return ResponseEntity.ok(meds);
    }

    /** GET /api/medications/patient/{patientId} */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Medication>> getMedicationsByPatient(@PathVariable Long patientId) {
        Patient patient = patientService.getPatientById(patientId);
        List<Medication> meds = medicationService.getMedicationsByPatient(patient);
        return ResponseEntity.ok(meds);
    }

    /** GET /api/medications/patient/{patientId}/active */
    @GetMapping("/patient/{patientId}/active")
    public ResponseEntity<List<Medication>> getActiveMedicationsByPatient(@PathVariable Long patientId) {
        Patient patient = patientService.getPatientById(patientId);
        List<Medication> meds = medicationService.getActiveMedicationsByPatient(patient);
        return ResponseEntity.ok(meds);
    }

    /** POST /api/medications */
    @PostMapping
    public ResponseEntity<Medication> createMedication(@RequestBody Medication medication) {
        Medication created = medicationService.createMedication(medication);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /** PUT /api/medications/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<Medication> updateMedication(
            @PathVariable Long id,
            @RequestBody Medication medicationDetails
    ) {
        Medication updated = medicationService.updateMedication(id, medicationDetails);
        return ResponseEntity.ok(updated);
    }

    /** DELETE /api/medications/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id) {
        medicationService.deleteMedication(id);
        return ResponseEntity.noContent().build();
    }
}
