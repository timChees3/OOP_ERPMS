package com.toinfinityandbeyond.RPMS.repository;

import com.toinfinityandbeyond.RPMS.model.Appointment;
import com.toinfinityandbeyond.RPMS.model.Doctor;
import com.toinfinityandbeyond.RPMS.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatient(Patient patient);

    List<Appointment> findByDoctor(Doctor doctor);

    List<Appointment> findByStatus(Appointment.Status status);

    List<Appointment> findByType(Appointment.Type type);

    List<Appointment> findByPatientAndStatus(Patient patient, Appointment.Status status);

    List<Appointment> findByDoctorAndStatus(Doctor doctor, Appointment.Status status);

    List<Appointment> findByScheduledTimeBetween(LocalDateTime start, LocalDateTime end);

    List<Appointment> findByDoctorAndScheduledTimeBetween(
            Doctor doctor, LocalDateTime start, LocalDateTime end);

    List<Appointment> findByPatientAndScheduledTimeBetween(
            Patient patient, LocalDateTime start, LocalDateTime end);

    List<Appointment> findByPatientOrderByScheduledTimeDesc(Patient patient);

    List<Appointment> findByDoctorOrderByScheduledTimeDesc(Doctor doctor);
}