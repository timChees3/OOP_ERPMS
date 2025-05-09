package com.toinfinityandbeyond.RPMS.service;

import com.toinfinityandbeyond.RPMS.exception.BadRequestException;
import com.toinfinityandbeyond.RPMS.exception.ResourceNotFoundException;
import com.toinfinityandbeyond.RPMS.model.*;
import com.toinfinityandbeyond.RPMS.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private MedicationService medicationService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private AlertService alertService;

    @Autowired
    private PatientService patientService;

    public List<Doctor> getAllDoctors()
    {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id)
    {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
    }

    public Doctor createDoctor(Doctor doctor)
    {
        return doctorRepository.save(doctor);
    }

    public Doctor updateDoctor(Long id, Doctor doctorDetails) {
        Doctor doctor = getDoctorById(id);

        // Update doctor-specific fields
        doctor.setSpecialization(doctorDetails.getSpecialization());
        doctor.setLicenseNumber(doctorDetails.getLicenseNumber());
        doctor.setQualification(doctorDetails.getQualification());
        doctor.setExperienceYears(doctorDetails.getExperienceYears());
        doctor.setConsultationFee(doctorDetails.getConsultationFee());

        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long id)
    {
        Doctor doctor = getDoctorById(id);
        doctorRepository.delete(doctor);
    }

    public List<Appointment> getDoctorAppointments(Long doctorId)
    {
        Doctor doctor = getDoctorById(doctorId);
        return appointmentService.getAppointmentsByDoctor(doctor);
    }

    public Appointment updateAppointmentStatus(Long doctorId, Long appointmentId, Appointment.Status status)
    {
        Doctor doctor = getDoctorById(doctorId);
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);

        // Verify the appointment belongs to the doctor
        if (!appointment.getDoctor().getId().equals(doctorId)) {
            throw new BadRequestException("Appointment does not belong to the doctor");
        }

        // Update status
        appointment.setStatus(status);
        Appointment updatedAppointment = appointmentService.updateAppointment(appointmentId, appointment);

        // Send email notification to patient
        alertService.sendAppointmentStatusUpdate(updatedAppointment);

        return updatedAppointment;
    }

    public Medication prescribeMedication(Long doctorId, Long appointmentId, Medication medication) {
        Doctor doctor = getDoctorById(doctorId);
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);

        // Verify the appointment belongs to the doctor
        if (!appointment.getDoctor().getId().equals(doctorId)) {
            throw new BadRequestException("Appointment does not belong to the doctor");
        }

        // Set appointment to the medication
        medication.setAppointment(appointment);

        Medication prescribedMedication = medicationService.createMedication(medication);

        return prescribedMedication;
    }

    public Feedback provideFeedback(Long doctorId, Long appointmentId, Feedback feedback)
    {
        Doctor doctor = getDoctorById(doctorId);
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);

        if (!appointment.getDoctor().getId().equals(doctorId)) {
            throw new BadRequestException("Appointment does not belong to the doctor");
        }

        feedback.setAppointment(appointment);

        Feedback savedFeedback = feedbackService.createFeedback(feedback);

        return savedFeedback;
    }

    public List<Patient> getDoctorPatients(Long doctorId)
    {
        Doctor doctor = getDoctorById(doctorId);
        return patientService.getPatientsByDoctor(doctorId);
    }

    public List<Doctor> getDoctorsBySpecialization(String specialization)
    {
        return doctorRepository.findBySpecialization(specialization);
    }
}