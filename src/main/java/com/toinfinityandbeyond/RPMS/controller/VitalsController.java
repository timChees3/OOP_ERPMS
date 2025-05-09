package com.toinfinityandbeyond.RPMS.controller;

import com.toinfinityandbeyond.RPMS.exception.ResourceNotFoundException;
import com.toinfinityandbeyond.RPMS.model.Patient;
import com.toinfinityandbeyond.RPMS.model.Vitals;
import com.toinfinityandbeyond.RPMS.repository.PatientRepository;
import com.toinfinityandbeyond.RPMS.service.VitalsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/vitals")
public class VitalsController {

    @Autowired
    private VitalsService vitalsService;

    @Autowired
    private PatientRepository patientRepository;

    /** GET /api/vitals */
    @GetMapping
    public List<Vitals> getAllVitals() {
        return vitalsService.getAllVitals();
    }

    /** GET /api/vitals/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<Vitals> getVitalsById(@PathVariable Long id) {
        Vitals v = vitalsService.getVitalsById(id);
        return ResponseEntity.ok(v);
    }

    /** GET /api/vitals/patient/{patientId} */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Vitals>> getByPatient(@PathVariable Long patientId) {
        Patient p = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        List<Vitals> list = vitalsService.getVitalsByPatient(p);
        return ResponseEntity.ok(list);
    }

    /**
     * GET /api/vitals/patient/{patientId}/range
     *   ?start=2025-05-01T00:00:00&end=2025-05-07T23:59:59
     */
    @GetMapping("/patient/{patientId}/range")
    public ResponseEntity<List<Vitals>> getByPatientAndDateRange(
            @PathVariable Long patientId,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        Patient p = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        List<Vitals> list = vitalsService.getVitalsByPatientAndDateRange(p, start, end);
        return ResponseEntity.ok(list);
    }

    /** POST /api/vitals */
    @PostMapping
    public ResponseEntity<Vitals> createVitals(@RequestBody Vitals vitals) {
        Vitals created = vitalsService.createVitals(vitals);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /** PUT /api/vitals/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<Vitals> updateVitals(
            @PathVariable Long id,
            @RequestBody Vitals vitalsDetails
    )
    {
        Vitals updated = vitalsService.updateVitals(id, vitalsDetails);
        return ResponseEntity.ok(updated);
    }

    /** DELETE /api/vitals/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVitals(@PathVariable Long id)
    {
        vitalsService.deleteVitals(id);
        return ResponseEntity.noContent().build();
    }
}
