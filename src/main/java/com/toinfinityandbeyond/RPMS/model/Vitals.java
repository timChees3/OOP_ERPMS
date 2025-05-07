package com.toinfinityandbeyond.RPMS.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vitals")
public class Vitals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "heart_rate")
    private Integer heartRate;

    @Column(name = "oxygen_level")
    private Double oxygenLevel;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "systolic_pressure")
    private Integer systolicPressure;

    @Column(name = "diastolic_pressure")
    private Integer diastolicPressure;

    @Column(name = "blood_sugar")
    private Double bloodSugar;

    @Column(name = "recorded_at")
    private LocalDateTime recordedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "is_critical")
    private boolean isCritical = false;

    @Column(name = "critical_notes", columnDefinition = "TEXT")
    private String criticalNotes;

    @Column(name = "alert_sent")
    private boolean alertSent = false;

    @PrePersist
    protected void onCreate()
    {
        this.recordedAt = LocalDateTime.now();
    }

    public void checkIfCritical()
    {
        StringBuilder notes = new StringBuilder();
        boolean critical = false;

        // Heart rate checks (normal: 60-100 bpm for adults)
        if (heartRate != null) {
            if (heartRate < 50 || heartRate > 120) {
                notes.append("Abnormal heart rate: ").append(heartRate).append(" bpm. ");
                critical = true;
            }
        }

        // Oxygen level checks (normal: >= 95%)
        if (oxygenLevel != null) {
            if (oxygenLevel < 92) {
                notes.append("Low oxygen level: ").append(oxygenLevel).append("%. ");
                critical = true;
            }
        }

        // Temperature checks (normal: 36.1-37.2°C)
        if (temperature != null) {
            if (temperature < 35.0 || temperature > 38.0) {
                notes.append("Abnormal temperature: ").append(temperature).append("°C. ");
                critical = true;
            }
        }

        // Blood pressure checks (normal: ~120/80 mmHg)
        if (systolicPressure != null && diastolicPressure != null) {
            if (systolicPressure > 140 || systolicPressure < 90 || diastolicPressure > 90 || diastolicPressure < 60) {
                notes.append("Abnormal blood pressure: ").append(systolicPressure).append("/").append(diastolicPressure).append(" mmHg. ");
                critical = true;
            }
        }

        // Blood sugar checks (depends on timing, but using general range)
        if (bloodSugar != null) {
            if (bloodSugar < 70 || bloodSugar > 180) {
                notes.append("Abnormal blood sugar: ").append(bloodSugar).append(" mg/dL. ");
                critical = true;
            }
        }

        this.isCritical = critical;
        if (critical) {
            this.criticalNotes = notes.toString();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public Double getOxygenLevel() {
        return oxygenLevel;
    }

    public void setOxygenLevel(Double oxygenLevel) {
        this.oxygenLevel = oxygenLevel;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getSystolicPressure() {
        return systolicPressure;
    }

    public void setSystolicPressure(Integer systolicPressure) {
        this.systolicPressure = systolicPressure;
    }

    public Integer getDiastolicPressure() {
        return diastolicPressure;
    }

    public void setDiastolicPressure(Integer diastolicPressure) {
        this.diastolicPressure = diastolicPressure;
    }

    public Double getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(Double bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public boolean isCritical() {
        return isCritical;
    }

    public void setCritical(boolean critical) {
        isCritical = critical;
    }

    public String getCriticalNotes() {
        return criticalNotes;
    }

    public void setCriticalNotes(String criticalNotes) {
        this.criticalNotes = criticalNotes;
    }

    public boolean isAlertSent() {
        return alertSent;
    }

    public void setAlertSent(boolean alertSent) {
        this.alertSent = alertSent;
    }
}