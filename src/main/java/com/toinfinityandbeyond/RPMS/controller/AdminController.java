package com.toinfinityandbeyond.RPMS.controller;


import com.toinfinityandbeyond.RPMS.model.Admin;
import com.toinfinityandbeyond.RPMS.model.User;
import com.toinfinityandbeyond.RPMS.model.Patient;
import com.toinfinityandbeyond.RPMS.model.Doctor;
import com.toinfinityandbeyond.RPMS.model.Appointment;
import com.toinfinityandbeyond.RPMS.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /** Admin CRUD **/
    /** GET /api/admins */
    @GetMapping
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    /** GET /api/admins/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getAdminById(id));
    }

    /** POST /api/admins */
    @PostMapping
    public ResponseEntity<Admin> createAdmin(@RequestBody Admin admin) {
        Admin created = adminService.createAdmin(admin);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /** PUT /api/admins/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<Admin> updateAdmin(
            @PathVariable Long id,
            @RequestBody Admin adminDetails
    ) {
        Admin updated = adminService.updateAdmin(id, adminDetails);
        return ResponseEntity.ok(updated);
    }

    /** DELETE /api/admins/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }

    /** User Management **/
    /** GET /api/admins/users */
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    /** GET /api/admins/users/{id} */
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    /** DELETE /api/admins/users/{id} */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /** Patient Management **/
    /** GET /api/admins/patients */
    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(adminService.getAllPatients());
    }

    /** GET /api/admins/patients/{id} */
    @GetMapping("/patients/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getPatientById(id));
    }

    /** DELETE /api/admins/patients/{id} */
    @DeleteMapping("/patients/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        adminService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    /** Doctor Management **/
    /** GET /api/admins/doctors */
    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(adminService.getAllDoctors());
    }

    /** GET /api/admins/doctors/{id} */
    @GetMapping("/doctors/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getDoctorById(id));
    }

    /** POST /api/admins/doctors */
    @PostMapping("/doctors")
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        Doctor created = adminService.createDoctor(doctor);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /** PUT /api/admins/doctors/{id} */
    @PutMapping("/doctors/{id}")
    public ResponseEntity<Doctor> updateDoctor(
            @PathVariable Long id,
            @RequestBody Doctor doctorDetails
    ) {
        Doctor updated = adminService.updateDoctor(id, doctorDetails);
        return ResponseEntity.ok(updated);
    }

    /** DELETE /api/admins/doctors/{id} */
    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        adminService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    /** Appointment Management **/
    /** GET /api/admins/appointments */
    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(adminService.getAllAppointments());
    }

    /** GET /api/admins/appointments/{id} */
    @GetMapping("/appointments/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getAppointmentById(id));
    }

    /** PUT /api/admins/appointments/{id}/status */
    @PutMapping("/appointments/{id}/status")
    public ResponseEntity<Appointment> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam("status") Appointment.Status status
    ) {
        Appointment updated = adminService.updateAppointmentStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    /** DELETE /api/admins/appointments/{id} */
    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        adminService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    /** System Statistics **/
    @GetMapping("/stats/patients-count")
    public ResponseEntity<Long> getTotalPatientsCount() {
        return ResponseEntity.ok(adminService.getTotalPatientsCount());
    }

    @GetMapping("/stats/doctors-count")
    public ResponseEntity<Long> getTotalDoctorsCount() {
        return ResponseEntity.ok(adminService.getTotalDoctorsCount());
    }

    @GetMapping("/stats/appointments-count")
    public ResponseEntity<Long> getTotalAppointmentsCount() {
        return ResponseEntity.ok(adminService.getTotalAppointmentsCount());
    }

    @GetMapping("/stats/pending-appointments-count")
    public ResponseEntity<Long> getPendingAppointmentsCount() {
        return ResponseEntity.ok(adminService.getPendingAppointmentsCount());
    }
}
