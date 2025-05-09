package com.toinfinityandbeyond.RPMS.service;

import com.toinfinityandbeyond.RPMS.exception.ResourceNotFoundException;
import com.toinfinityandbeyond.RPMS.model.*;
import com.toinfinityandbeyond.RPMS.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AlertService alertService;


    public List<Admin> getAllAdmins()
    {
        return adminRepository.findAll();
    }

    public Admin getAdminById(Long id)
    {
        return adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));
    }

    public Admin createAdmin(Admin admin)
    {
        return adminRepository.save(admin);
    }

    public Admin updateAdmin(Long id, Admin adminDetails)
    {
        Admin admin = getAdminById(id);

        // Update admin-specific fields
        admin.setDepartment(adminDetails.getDepartment());
        admin.setPosition(adminDetails.getPosition());

        return adminRepository.save(admin);
    }

    public void deleteAdmin(Long id)
    {
        Admin admin = getAdminById(id);
        adminRepository.delete(admin);
    }

    // User management
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    public User getUserById(Long id) {
        return userService.getUserById(id);
    }

    public void deleteUser(Long id) {
        userService.deleteUser(id);
    }

    // Patient management
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    public Patient getPatientById(Long id) {
        return patientService.getPatientById(id);
    }

    public void deletePatient(Long id) {
        patientService.deletePatient(id);
    }

    // Doctor management
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    public Doctor getDoctorById(Long id) {
        return doctorService.getDoctorById(id);
    }

    public Doctor createDoctor(Doctor doctor) {
        return doctorService.createDoctor(doctor);
    }

    public Doctor updateDoctor(Long id, Doctor doctorDetails) {
        return doctorService.updateDoctor(id, doctorDetails);
    }

    public void deleteDoctor(Long id) {
        doctorService.deleteDoctor(id);
    }

    // Appointment management
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentService.getAppointmentById(id);
    }

    public Appointment updateAppointmentStatus(Long appointmentId, Appointment.Status status) {
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        appointment.setStatus(status);

        Appointment updatedAppointment = appointmentService.updateAppointment(appointmentId, appointment);

        // Notify doctor and patient about the appointment status change
        alertService.sendAppointmentStatusUpdate(updatedAppointment);

        return updatedAppointment;
    }

    public void deleteAppointment(Long id) {
        appointmentService.deleteAppointment(id);
    }

    // System statistics
    public long getTotalPatientsCount() {
        return patientService.getAllPatients().size();
    }

    public long getTotalDoctorsCount() {
        return doctorService.getAllDoctors().size();
    }

    public long getTotalAppointmentsCount() {
        return appointmentService.getAllAppointments().size();
    }

    public long getPendingAppointmentsCount() {
        return appointmentService.getAppointmentsByStatus(Appointment.Status.REQUESTED).size();
    }
}