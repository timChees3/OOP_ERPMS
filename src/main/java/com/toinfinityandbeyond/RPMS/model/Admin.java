package com.toinfinityandbeyond.RPMS.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admins")
public class Admin extends User
{

    @Column(name = "department")
    private String department;

    @Column(name = "position")
    private String position;

    @Column(name = "admin_level")
    private Integer adminLevel;

    @Column(name = "employee_id", unique = true)
    private String employeeId;

    @PrePersist
    @Override
    protected void onCreate() {
        super.onCreate();
        getRoles().add("ROLE_ADMIN");
    }
}