package com.toinfinityandbeyond.RPMS.service;

import com.toinfinityandbeyond.RPMS.exception.ResourceNotFoundException;
import com.toinfinityandbeyond.RPMS.model.Appointment;
import com.toinfinityandbeyond.RPMS.model.Doctor;
import com.toinfinityandbeyond.RPMS.model.Patient;
import com.toinfinityandbeyond.RPMS.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AlertService alertService;


    public List<Appointment> getAllAppointments()
    {

        return appointmentRepository.findAll();
    }

    public Appointment getAppointmentById(Long id)
    {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
    }

    public List<Appointment> getAppointmentsByPatient(Patient patient)
    {
        return appointmentRepository.findByPatientOrderByScheduledTimeDesc(patient);
    }

    public List<Appointment> getAppointmentsByDoctor(Doctor doctor)
    {
        return appointmentRepository.findByDoctorOrderByScheduledTimeDesc(doctor);
    }

    public List<Appointment> getAppointmentsByStatus(Appointment.Status status) {
        return appointmentRepository.findByStatus(status);
    }

    public List<Appointment> getUpcomingAppointmentsByPatient(Patient patient) {
        LocalDateTime currentDate = LocalDateTime.now();
        return appointmentRepository.findByPatientAndScheduledTimeAfterOrderByScheduledTimeAsc(patient, currentDate);
    }

    public List<Appointment> getUpcomingAppointmentsByDoctor(Doctor doctor) {
        LocalDateTime currentDate = LocalDateTime.now();
        return appointmentRepository.findByDoctorAndScheduledTimeAfterOrderByScheduledTimeAsc(doctor, currentDate);
    }

    public Appointment createAppointment(Appointment appointment) {

        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Send email notifications
        alertService.sendAppointmentRequestNotificationToDoctor(savedAppointment);
        alertService.sendAppointmentRequestConfirmationToPatient(savedAppointment);

        return savedAppointment;
    }

    public Appointment updateAppointment(Long id, Appointment appointmentDetails) {
        Appointment appointment = getAppointmentById(id);

        // Update appointment details
        appointment.setDoctor(appointmentDetails.getDoctor());
        appointment.setScheduledTime(appointmentDetails.getScheduledTime());
        appointment.setType(appointmentDetails.getType());
        appointment.setReason(appointmentDetails.getReason());
        appointment.setNotes(appointmentDetails.getNotes());

        // If status is being updated
        if (appointmentDetails.getStatus() != null && !appointmentDetails.getStatus().equals(appointment.getStatus())) {
            appointment.setStatus(appointmentDetails.getStatus());

            alertService.sendAppointmentStatusUpdate(appointment);
        }

        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Long id) {
        Appointment appointment = getAppointmentById(id);
        appointmentRepository.delete(appointment);
    }

    public boolean isDoctorAvailable(Doctor doctor, LocalDateTime scheduledAt) {
        return !appointmentRepository.existsByDoctorAndScheduledTime(doctor, scheduledAt);
    }


    public List<Appointment> getTodayAppointments()
    {
        return appointmentRepository.findTodayAppointments();
    }
}