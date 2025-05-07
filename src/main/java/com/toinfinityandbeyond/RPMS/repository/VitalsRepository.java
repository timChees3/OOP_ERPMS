package com.toinfinityandbeyond.RPMS.repository;

import com.toinfinityandbeyond.RPMS.model.Patient;
import com.toinfinityandbeyond.RPMS.model.Vitals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VitalsRepository extends JpaRepository<Vitals, Long> {

    List<Vitals> findByPatient(Patient patient);

    List<Vitals> findByPatientAndRecordedAtBetween(Patient patient, LocalDateTime startDate, LocalDateTime endDate);

    List<Vitals> findByPatientAndIsCriticalTrue(Patient patient);

    List<Vitals> findByPatientOrderByRecordedAtDesc(Patient patient);

    @Query("SELECT v FROM Vitals v WHERE v.patient.id = :patientId ORDER BY v.recordedAt DESC LIMIT 1")
    Vitals findLatestByPatientId(Long patientId);

    List<Vitals> findByIsCriticalTrueAndAlertSentFalse();
}