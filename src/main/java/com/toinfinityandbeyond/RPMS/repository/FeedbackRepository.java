package com.toinfinityandbeyond.RPMS.repository;

import com.toinfinityandbeyond.RPMS.model.Appointment;
import com.toinfinityandbeyond.RPMS.model.Doctor;
import com.toinfinityandbeyond.RPMS.model.Feedback;
import com.toinfinityandbeyond.RPMS.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByPatient(Patient patient);

    List<Feedback> findByDoctor(Doctor doctor);

    Optional<Feedback> findByAppointment(Appointment appointment);

    List<Feedback> findByPatientOrderByCreatedAtDesc(Patient patient);

    List<Feedback> findByDoctorOrderByCreatedAtDesc(Doctor doctor);

    boolean existsByAppointment(Appointment appointment);

    List<Appointment> findAppointmentsWithoutFeedbackByDoctor(Doctor doctor);
}
