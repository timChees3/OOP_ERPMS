package com.toinfinityandbeyond.RPMS.service;

import com.toinfinityandbeyond.RPMS.exception.ResourceNotFoundException;
import com.toinfinityandbeyond.RPMS.model.*;
import com.toinfinityandbeyond.RPMS.repository.ReportRepository;
import com.toinfinityandbeyond.RPMS.repository.VitalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private VitalsService vitalsService;

    @Autowired
    private MedicationService medicationService;

    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private VitalsRepository vitalsRepository;

    public List<Report> getAllReports()
    {
        return reportRepository.findAll();
    }

    public Report getReportById(Long id)
    {
        return reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));
    }

    public List<Report> getReportsByPatient(Patient patient) {
        return reportRepository.findByPatientOrderByGeneratedAtDesc(patient);
    }

    public Report generateReport(Appointment appointment)
    {
        // Check if feedback exists for this appointment
        Feedback feedback = feedbackService.getFeedbackByAppointment(appointment);
        if (feedback == null) {
            throw new ResourceNotFoundException("No feedback found for this appointment");
        }

        Vitals latestVitals = vitalsRepository.findLatestByPatientId(appointment.getPatient().getId());

        List<Medication> medications = medicationService.getMedicationsByAppointment(appointment);

        // Create a new report
        Report report = new Report();
        report.setAppointment(appointment);
        report.setPatient(appointment.getPatient());
        report.setDoctor(appointment.getDoctor());
        report.setFeedback(feedback);
        report.setVitals(latestVitals);
        report.setMedications(medications);

        return reportRepository.save(report);
    }

    public Report updateReport(Long id, Report reportDetails) {
        Report report = getReportById(id);

        // Update report details
        report.setTitle(reportDetails.getTitle());
        report.setSummary(reportDetails.getSummary());
        report.setReportType(reportDetails.getReportType());

        return reportRepository.save(report);
    }

    public void deleteReport(Long id)
    {
        Report report = getReportById(id);
        reportRepository.delete(report);
    }


    public List<Report> getReportsByPatientBetween(Patient patient, LocalDateTime start, LocalDateTime end)
    {
        return reportRepository.findByPatientAndGeneratedAtBetween(patient, start, end);
    }
}