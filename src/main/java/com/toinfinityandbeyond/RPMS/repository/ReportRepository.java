package com.toinfinityandbeyond.RPMS.repository;

import com.toinfinityandbeyond.RPMS.model.Doctor;
import com.toinfinityandbeyond.RPMS.model.Patient;
import com.toinfinityandbeyond.RPMS.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByPatient(Patient patient);

    List<Report> findByDoctor(Doctor doctor);

    List<Report> findByReportType(String reportType);

    List<Report> findByPatientAndReportType(Patient patient, String reportType);

    List<Report> findByPatientAndGeneratedAtBetween(
            Patient patient, LocalDateTime start, LocalDateTime end);

    List<Report> findByPatientOrderByGeneratedAtDesc(Patient patient);
}