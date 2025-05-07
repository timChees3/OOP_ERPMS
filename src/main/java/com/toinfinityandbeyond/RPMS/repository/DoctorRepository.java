package com.toinfinityandbeyond.RPMS.repository;

import com.toinfinityandbeyond.RPMS.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findBySpecialization(String specialization);

    List<Doctor> findByActiveTrue();

    List<Doctor> findByAvailableForEmergencyTrue();

    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    List<Doctor> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName);
}