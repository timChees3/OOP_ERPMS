package com.toinfinityandbeyond.RPMS.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3)
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank
    @Size(min = 8)
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Column(nullable = false)
    private String fName;

    @NotBlank
    @Column(nullable = false)
    private String lName;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "^(\\+92|92|0)3[0-9]{9}$")
    private String phoneNumber;

    @NotBlank
    @Pattern(regexp = "^(MALE|FEMALE|OTHER)$")
    @Column(nullable = false)
    private String gender;

    @NotNull
    @Past
    @Column(nullable = false)
    private LocalDate dob;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    private String address;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    @Column(nullable = false)
    private LocalDateTime accountCreationDate;

    @PrePersist
    protected void onCreate() {
        this.accountCreationDate = LocalDateTime.now();
    }

    /* --------------- */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank @Size(min = 3) String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank @Size(min = 3) String username) {
        this.username = username;
    }

    public @NotBlank @Size(min = 8) String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Size(min = 8) String password) {
        this.password = password;
    }

    public @NotBlank String getfName() {
        return fName;
    }

    public void setfName(@NotBlank String fName) {
        this.fName = fName;
    }

    public @NotBlank String getlName() {
        return lName;
    }

    public void setlName(@NotBlank String lName) {
        this.lName = lName;
    }

    public @Email @NotBlank String getEmail() {
        return email;
    }

    public void setEmail(@Email @NotBlank String email) {
        this.email = email;
    }

    public @Pattern(regexp = "^(\\+92|92|0)3[0-9]{9}$") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@Pattern(regexp = "^(\\+92|92|0)3[0-9]{9}$") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NotBlank @Pattern(regexp = "^(MALE|FEMALE|OTHER)$") String getGender() {
        return gender;
    }

    public void setGender(@NotBlank @Pattern(regexp = "^(MALE|FEMALE|OTHER)$") String gender) {
        this.gender = gender;
    }

    public @NotNull @Past LocalDate getDob() {
        return dob;
    }

    public void setDob(@NotNull @Past LocalDate dob) {
        this.dob = dob;
    }

    public @NotBlank @Size(max = 255) String getAddress() {
        return address;
    }

    public void setAddress(@NotBlank @Size(max = 255) String address) {
        this.address = address;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public LocalDateTime getAccountCreationDate() {
        return accountCreationDate;
    }

    public void setAccountCreationDate(LocalDateTime accountCreationDate) {
        this.accountCreationDate = accountCreationDate;
    }
}

