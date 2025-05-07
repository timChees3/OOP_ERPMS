package com.toinfinityandbeyond.RPMS.service;

import com.toinfinityandbeyond.RPMS.model.Patient;
import com.toinfinityandbeyond.RPMS.model.Vitals;
import com.toinfinityandbeyond.RPMS.repository.PatientRepository;
import com.toinfinityandbeyond.RPMS.repository.VitalsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VitalsService {

    @Autowired
    private  VitalsRepository vitalsRepository;

    @Autowired
    private  PatientRepository patientRepository;

    @Autowired
    private  AlertService alertService;

    public Vitals save(Vitals vitals) {
        // Check if the vitals are critical
        vitals.checkIfCritical();

        // Save the vitals
        Vitals savedVitals = vitalsRepository.save(vitals);

        // Send alert if vitals are critical
        if (savedVitals.isCritical() && !savedVitals.isAlertSent()) {
            alertService.sendVitalsAlert(savedVitals);
            savedVitals.setAlertSent(true);
            vitalsRepository.save(savedVitals);
        }

        return savedVitals;
    }

    public Vitals findById(Long id) {
        return vitalsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vitals not found with id: " + id));
    }

    public List<Vitals> findByPatientId(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + patientId));
        return vitalsRepository.findByPatient(patient);
    }

    public List<Vitals> findByPatientIdAndDateRange(Long patientId, LocalDate startDate, LocalDate endDate) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + patientId));

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        return vitalsRepository.findByPatientAndRecordedAtBetween(patient, startDateTime, endDateTime);
    }

    public Vitals findLatestByPatientId(Long patientId) {
        return vitalsRepository.findLatestByPatientId(patientId);
    }

    public List<Vitals> findCriticalByPatientId(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + patientId));
        return vitalsRepository.findByPatientAndIsCriticalTrue(patient);
    }

    public void deleteById(Long id) {
        vitalsRepository.deleteById(id);
    }

    @Transactional
    public List<Vitals> uploadVitalsFromCsv(MultipartFile file, Long patientId) throws IOException {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + patientId));

        List<Vitals> vitalsList = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            // Skip header
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                if (values.length < 7) { // Ensure the CSV has all required fields
                    continue;
                }

                Vitals vitals = new Vitals();
                vitals.setPatient(patient);

                // Parse values from CSV
                if (!values[0].isEmpty()) vitals.setRecordedAt(LocalDateTime.parse(values[0], dateTimeFormatter));
                if (!values[1].isEmpty()) vitals.setHeartRate(Integer.parseInt(values[1].trim()));
                if (!values[2].isEmpty()) vitals.setOxygenLevel(Double.parseDouble(values[2].trim()));
                if (!values[3].isEmpty()) vitals.setTemperature(Double.parseDouble(values[3].trim()));
                if (!values[4].isEmpty()) vitals.setSystolicPressure(Integer.parseInt(values[4].trim()));
                if (!values[5].isEmpty()) vitals.setDiastolicPressure(Integer.parseInt(values[5].trim()));
                if (values.length > 7 && !values[7].isEmpty()) vitals.setBloodSugar(Double.parseDouble(values[7].trim()));

                // Check if vitals are critical
                vitals.checkIfCritical();

                // Save to database
                Vitals savedVitals = vitalsRepository.save(vitals);
                vitalsList.add(savedVitals);

                // Send alert if vitals are critical
                if (savedVitals.isCritical()) {
                    alertService.sendVitalsAlert(savedVitals);
                    savedVitals.setAlertSent(true);
                    vitalsRepository.save(savedVitals);
                }
            }
        }

        return vitalsList;
    }

    @Transactional
    public void checkForUnsentAlerts() {
        List<Vitals> criticalUnalertedVitals = vitalsRepository.findByIsCriticalTrueAndAlertSentFalse();

        for (Vitals vitals : criticalUnalertedVitals) {
            alertService.sendVitalsAlert(vitals);
            vitals.setAlertSent(true);
            vitalsRepository.save(vitals);
        }
    }
}