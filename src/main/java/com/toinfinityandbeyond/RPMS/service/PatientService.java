package com.toinfinityandbeyond.RPMS.service;

import com.toinfinityandbeyond.RPMS.exception.BadRequestException;
import com.toinfinityandbeyond.RPMS.exception.ResourceNotFoundException;
import com.toinfinityandbeyond.RPMS.model.Appointment;
import com.toinfinityandbeyond.RPMS.model.Patient;
import com.toinfinityandbeyond.RPMS.model.Report;
import com.toinfinityandbeyond.RPMS.model.Vitals;
import com.toinfinityandbeyond.RPMS.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private VitalsService vitalsService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private AlertService alertService;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
    }

    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, Patient patientDetails) {
        Patient patient = getPatientById(id);

        // Update patient-specific fields
        patient.setBloodGroup(patientDetails.getBloodGroup());
        patient.setEmergencyContact(patientDetails.getEmergencyContact());
        patient.setAllergies(patientDetails.getAllergies());
        patient.setPrimaryDoctor(patientDetails.getPrimaryDoctor());

        return patientRepository.save(patient);
    }

    public void deletePatient(Long id) {
        Patient patient = getPatientById(id);
        patientRepository.delete(patient);
    }

    public Appointment requestAppointment(Long patientId, Appointment appointment) {
        Patient patient = getPatientById(patientId);
        appointment.setPatient(patient);
        appointment.setStatus(Appointment.Status.REQUESTED);

        return appointmentService.createAppointment(appointment);
    }

    public Vitals addVitals(Long patientId, Vitals vitals) {
        Patient patient = getPatientById(patientId);
        vitals.setPatient(patient);

        Vitals savedVitals = vitalsService.createVitals(vitals);

        return savedVitals;
    }

    public List<Vitals> getPatientVitals(Long patientId) {
        Patient patient = getPatientById(patientId);
        return vitalsService.getVitalsByPatient(patient);
    }

    public List<Appointment> getPatientAppointments(Long patientId) {
        Patient patient = getPatientById(patientId);
        return appointmentService.getAppointmentsByPatient(patient);
    }

    public List<Report> getPatientReports(Long patientId) {
        Patient patient = getPatientById(patientId);
        return reportService.getReportsByPatient(patient);
    }

    public List<Patient> getPatientsByDoctor(Long doctorId)
    {
        return patientRepository.findByAppointmentsDoctorId(doctorId);
    }
}