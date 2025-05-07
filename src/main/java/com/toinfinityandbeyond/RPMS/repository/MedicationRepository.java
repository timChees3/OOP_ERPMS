package com.toinfinityandbeyond.RPMS.repository;

import com.toinfinityandbeyond.RPMS.model.Appointment;
import com.toinfinityandbeyond.RPMS.model.Doctor;
import com.toinfinityandbeyond.RPMS.model.Medication;
import com.toinfinityandbeyond.RPMS.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {

    List<Medication> findByPatient(Patient patient);

    List<Medication> findByDoctor(Doctor doctor);

    List<Medication> findByAppointment(Appointment appointment);

    List<Medication> findByPatientAndActiveTrue(Patient patient);

    List<Medication> findByPatientAndStartDateBeforeAndEndDateAfter(
            Patient patient, LocalDate currentDate, LocalDate currentDate2);

    List<Medication> findByPatientOrderByPrescribedAtDesc(Patient patient);
}