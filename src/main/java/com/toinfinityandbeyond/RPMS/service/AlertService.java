package com.toinfinityandbeyond.RPMS.service;

import com.toinfinityandbeyond.RPMS.model.Appointment;
import com.toinfinityandbeyond.RPMS.model.Medication;
import com.toinfinityandbeyond.RPMS.model.Patient;
import com.toinfinityandbeyond.RPMS.model.Vitals;
import com.toinfinityandbeyond.RPMS.repository.AppointmentRepository;
import com.toinfinityandbeyond.RPMS.repository.MedicationRepository;
import com.toinfinityandbeyond.RPMS.repository.VitalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AlertService
{
    @Autowired
    private VitalsRepository vitalsRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MedicationRepository medicationRepository;

    @Value("${spring.mail.from:no-reply@rpms.local}")
    private String fromAddress;

    public void sendAppointmentStatusUpdate(Appointment appt) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(fromAddress);
        msg.setTo(appt.getPatient().getEmail());
        msg.setSubject("Your appointment on " + appt.getScheduledTime() + " is now " + appt.getStatus().name());
        if (Appointment.Type.VIDEO_CONSULTATION.equals(appt.getType()))
        {
            msg.setText("Hello " + appt.getPatient().getfName() + ",\n\n"
                    + "Your appointment scheduled for " + appt.getScheduledTime()
                    + " has been marked as “" + appt.getStatus() + "”.\n"
                    + "Meeting Link: https://meet.google.com/jrv-qroc-zkc" + "”.\n\n"
                    + "Best,\nThe ERPMS Team");
        }
        else
        {
            msg.setText("Hello " + appt.getPatient().getfName() + ",\n\n"
                    + "Your appointment scheduled for " + appt.getScheduledTime()
                    + " has been marked as “" + appt.getStatus() + "”.\n\n"
                    + "Best,\nThe ERPMS Team");
        }

        mailSender.send(msg);
    }

    @Transactional
    public void sendCriticalVitalsAlerts(Vitals v)
    {
        Patient p = v.getPatient();
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(fromAddress);
        msg.setTo(p.getPrimaryDoctor().getEmail());
        msg.setSubject("EMERGENCY: Critical vitals for " + p.getfName() + " " + p.getlName());
        msg.setText("Doctor,\n\n"
                + "Patient " + p.getfName() + " has got critical "
                + v.getCriticalNotes()
                + " at " + v.getRecordedAt() + ".\n\n"
                + "Please attend immediately.\n\n"
                + "– ERPMS Alert System");
        mailSender.send(msg);

        v.setAlertSent(true);
        vitalsRepository.save(v);
    }


    public void panicButtonPressed(Patient patient)
    {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(fromAddress);
        // assume Patient has a method getOnCallTeamEmails()
        msg.setTo(patient.getPrimaryDoctor().getEmail());
        msg.setSubject("PANIC BUTTON TRIGGERED for " +  patient.getfName() + " " + patient.getlName());
        msg.setText("Team,\n\n"
                + "The panic button was pressed by "
                + patient.getfName() + " " + patient.getlName() + " (ID: " + patient.getId() + ").\n\n"
                + "Please respond immediately.\n\n"
                + "– ERPMS Alert System");
        mailSender.send(msg);
    }

    @Scheduled(cron = "0 0 8 * * *")
    @Transactional
    public void sendDailyMedicationReminders() {
        LocalDate today = LocalDate.now();
        List<Medication> meds = medicationRepository
                .findByEndDateAfter(today);

        for (Medication med : meds) {
            sendMedicationReminder(med);
        }
    }

    public void sendMedicationReminder(Medication med) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(fromAddress);
        msg.setTo(med.getPatient().getEmail());
        msg.setSubject("Medication Reminder: “" + med.getName() + "”");
        msg.setText(
                "Hello " + med.getPatient().getfName() + ",\n\n" +
                        "This is your reminder to take your medication:\n\n" +
                        "• Name: " + med.getName() + "\n" +
                        "• Dosage: " + med.getDosage() + "\n" +
                        "• Instructions: " + med.getInstructions() + "\n\n" +
                        "Please follow your prescribed schedule (" + med.getFrequency() + ").\n\n" +
                        "If you have any questions, contact your doctor.\n\n" +
                        "– The ERPMS Team"
        );
        mailSender.send(msg);
    }

    public void sendAppointmentRequestNotificationToDoctor(Appointment appt) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(fromAddress);
        msg.setTo(appt.getDoctor().getEmail());
        msg.setSubject("New Appointment Request from " + appt.getPatient().getfName() + " " + appt.getPatient().getlName());
        msg.setText(
                "Hello Dr. " + appt.getDoctor().getlName() + ",\n\n" +
                        appt.getPatient().getfName() + " " + appt.getPatient().getlName() +
                        " has requested an appointment on " + appt.getScheduledTime() + ".\n" +
                        "Please review and confirm or decline the request in the system.\n\n" +
                        "– ERPMS Alert System"
        );
        mailSender.send(msg);
    }

    public void sendAppointmentRequestConfirmationToPatient(Appointment appt) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(fromAddress);
        msg.setTo(appt.getPatient().getEmail());
        msg.setSubject("Appointment Request Received");
        msg.setText(
                "Hello " + appt.getPatient().getfName() + " " + appt.getPatient().getlName() + ",\n\n" +
                        "We have received your appointment request for " + appt.getScheduledTime() + ".\n" +
                        "The doctor will be notified and you will receive a confirmation email once the request is approved.\n\n" +
                        "Thank you,\nThe ERPMS Team"
        );
        mailSender.send(msg);
    }

    public void sendEmail()
    {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(fromAddress);
        msg.setTo("amoiz25pk@gmail.com");
        msg.setSubject("New Appointment Request");
        msg.setText("Hello");
        mailSender.send(msg);
    }
}
