package com.toinfinityandbeyond.RPMS.service;

import com.toinfinityandbeyond.RPMS.exception.ResourceNotFoundException;
import com.toinfinityandbeyond.RPMS.model.Patient;
import com.toinfinityandbeyond.RPMS.model.Vitals;
import com.toinfinityandbeyond.RPMS.repository.VitalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VitalsService {

    @Autowired
    private VitalsRepository vitalsRepository;

    @Autowired
    private AlertService alertService;

    public List<Vitals> getAllVitals()
    {
        return vitalsRepository.findAll();
    }

    public Vitals getVitalsById(Long id) {
        return vitalsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vitals not found with id: " + id));
    }

    public List<Vitals> getVitalsByPatient(Patient patient) {
        return vitalsRepository.findByPatientOrderByRecordedAtDesc(patient);
    }

    public List<Vitals> getVitalsByPatientAndDateRange(Patient patient, LocalDateTime startDate, LocalDateTime endDate) {
        return vitalsRepository.findByPatientAndRecordedAtBetween(patient, startDate, endDate);
    }

    public Vitals createVitals(Vitals vitals)
    {
        Vitals savedVitals = vitalsRepository.save(vitals);

        if (vitals.isCritical()) {
            alertService.sendCriticalVitalsAlerts(vitals);
        }
        return savedVitals;
    }

    public Vitals updateVitals(Long id, Vitals vitalsDetails) {
        Vitals vitals = getVitalsById(id);

        // Update vitals data
        vitals.setHeartRate(vitalsDetails.getHeartRate());
        vitals.setSystolicPressure(vitalsDetails.getSystolicPressure());
        vitals.setDiastolicPressure(vitalsDetails.getDiastolicPressure());
        vitals.setTemperature(vitalsDetails.getTemperature());
        vitals.setOxygenLevel(vitalsDetails.getOxygenLevel());
        vitals.setBloodSugar(vitalsDetails.getBloodSugar());
        vitals.setCriticalNotes(vitalsDetails.getCriticalNotes());


        Vitals updatedVitals = vitalsRepository.save(vitals);

        if (vitals.isCritical())
        {
            alertService.sendCriticalVitalsAlerts(updatedVitals);
        }

        return updatedVitals;
    }

    public void deleteVitals(Long id)
    {
        Vitals vitals = getVitalsById(id);
        vitalsRepository.delete(vitals);
    }
}