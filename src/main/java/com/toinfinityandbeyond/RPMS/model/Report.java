package com.toinfinityandbeyond.RPMS.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "report_type")
    private String reportType;

    @ManyToMany
    @JoinTable(
            name = "report_vitals",
            joinColumns = @JoinColumn(name = "report_id"),
            inverseJoinColumns = @JoinColumn(name = "vitals_id")
    )
    private List<Vitals> vitalsList = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "report_medications",
            joinColumns = @JoinColumn(name = "report_id"),
            inverseJoinColumns = @JoinColumn(name = "medication_id")
    )
    private List<Medication> medications = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "report_feedbacks",
            joinColumns = @JoinColumn(name = "report_id"),
            inverseJoinColumns = @JoinColumn(name = "feedback_id")
    )
    private List<Feedback> feedbacks = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "report_appointments",
            joinColumns = @JoinColumn(name = "report_id"),
            inverseJoinColumns = @JoinColumn(name = "appointment_id")
    )
    private List<Appointment> appointments = new ArrayList<>();

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "time_period_start")
    private LocalDateTime timePeriodStart;

    @Column(name = "time_period_end")
    private LocalDateTime timePeriodEnd;

    @PrePersist
    protected void onCreate() {
        this.generatedAt = LocalDateTime.now();
    }


}