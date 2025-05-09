package com.toinfinityandbeyond.RPMS.service;

import com.toinfinityandbeyond.RPMS.exception.ResourceNotFoundException;
import com.toinfinityandbeyond.RPMS.model.Appointment;
import com.toinfinityandbeyond.RPMS.model.Medication;
import com.toinfinityandbeyond.RPMS.model.Patient;
import com.toinfinityandbeyond.RPMS.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MedicationService {

    @Autowired
    private MedicationRepository medicationRepository;

    public List<Medication> getAllMedications()
    {
        return medicationRepository.findAll();
    }

    public Medication getMedicationById(Long id)
    {
        return medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found with id: " + id));
    }

    public List<Medication> getMedicationsByAppointment(Appointment appointment)
    {
        return medicationRepository.findByAppointment(appointment);
    }

    public List<Medication> getMedicationsByPatient(Patient patient)
    {
        return medicationRepository.findByPatient(patient);
    }

    public List<Medication> getActiveMedicationsByPatient(Patient patient)
    {
        LocalDate currentDate = LocalDate.now();
        return medicationRepository.findByAppointmentPatientAndEndDateAfter(patient, currentDate);
    }

    public Medication createMedication(Medication medication)
    {
        Medication savedMedication = medicationRepository.save(medication);

        return savedMedication;
    }

    public Medication updateMedication(Long id, Medication medicationDetails) {
        Medication medication = getMedicationById(id);

        // Update medication details
        medication.setName(medicationDetails.getName());
        medication.setDosage(medicationDetails.getDosage());
        medication.setFrequency(medicationDetails.getFrequency());
        medication.setStartDate(medicationDetails.getStartDate());
        medication.setEndDate(medicationDetails.getEndDate());
        medication.setInstructions(medicationDetails.getInstructions());
        medication.setNotes(medicationDetails.getNotes());

        Medication updatedMedication = medicationRepository.save(medication);

        return updatedMedication;
    }

    public void deleteMedication(Long id)
    {
        Medication medication = getMedicationById(id);
        medicationRepository.delete(medication);
    }
}
