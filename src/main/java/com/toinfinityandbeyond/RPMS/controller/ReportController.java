package com.toinfinityandbeyond.RPMS.controller;

import com.toinfinityandbeyond.RPMS.exception.ResourceNotFoundException;
import com.toinfinityandbeyond.RPMS.model.Appointment;
import com.toinfinityandbeyond.RPMS.model.Patient;
import com.toinfinityandbeyond.RPMS.model.Report;
import com.toinfinityandbeyond.RPMS.service.AppointmentService;
import com.toinfinityandbeyond.RPMS.service.PatientService;
import com.toinfinityandbeyond.RPMS.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;

    /** GET /api/reports */
    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    /** GET /api/reports/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable Long id) {
        Report report = reportService.getReportById(id);
        return ResponseEntity.ok(report);
    }

    /** GET /api/reports/patient/{patientId} */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Report>> getReportsByPatient(@PathVariable Long patientId) {
        Patient patient = patientService.getPatientById(patientId);
        List<Report> reports = reportService.getReportsByPatient(patient);
        return ResponseEntity.ok(reports);
    }

    /** GET /api/reports/patient/{patientId}/range */
    @GetMapping("/patient/{patientId}/range")
    public ResponseEntity<List<Report>> getReportsByPatientBetween(
            @PathVariable Long patientId,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        Patient patient = patientService.getPatientById(patientId);
        List<Report> reports = reportService.getReportsByPatientBetween(patient, start, end);
        return ResponseEntity.ok(reports);
    }

    /** POST /api/reports/appointment/{appointmentId} */
    @PostMapping("/appointment/{appointmentId}")
    public ResponseEntity<Report> generateReport(@PathVariable Long appointmentId) {
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        Report report = reportService.generateReport(appointment);
        return new ResponseEntity<>(report, HttpStatus.CREATED);
    }

    /** PUT /api/reports/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<Report> updateReport(
            @PathVariable Long id,
            @RequestBody Report reportDetails
    ) {
        Report updated = reportService.updateReport(id, reportDetails);
        return ResponseEntity.ok(updated);
    }

    /** DELETE /api/reports/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}
