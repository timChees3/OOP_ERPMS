package com.toinfinityandbeyond.RPMS.service;

import com.toinfinityandbeyond.RPMS.exception.BadRequestException;
import com.toinfinityandbeyond.RPMS.exception.ResourceNotFoundException;
import com.toinfinityandbeyond.RPMS.model.Admin;
import com.toinfinityandbeyond.RPMS.model.Doctor;
import com.toinfinityandbeyond.RPMS.model.Patient;
import com.toinfinityandbeyond.RPMS.model.User;
import com.toinfinityandbeyond.RPMS.repository.UserRepository;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User signup(User user) {
        // decide subclass by role
        if (user.getRoles().contains("PATIENT")) {
            Patient p = new Patient();
            copyBase(user, p);
            return userRepository.save(p);
        }
        else if (user.getRoles().contains("DOCTOR")) {
            Doctor d = new Doctor();
            copyBase(user, d);
            return userRepository.save(d);
        }
        else if (user.getRoles().contains("ADMIN")) {
            Admin a = new Admin();
            copyBase(user, a);
            return userRepository.save(a);
        }
        else
        {
            throw new BadRequestException("Invalid role");
        }
    }

    public Map<String,Object> login(String username, String password) {
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("Invalid username or password"));
        if (!u.getPassword().equals(password)) {
            throw new BadRequestException("Invalid username or password");
        }
        // return both id and roles
        return Map.of(
                "id", u.getId(),
                "roles", u.getRoles()
        );
    }

    private void copyBase(User src, User dest)
    {
        dest.setUsername(src.getUsername());
        dest.setPassword(src.getPassword());
        dest.setfName(src.getfName());
        dest.setlName(src.getlName());
        dest.setEmail(src.getEmail());
        dest.setPhoneNumber(src.getPhoneNumber());
        dest.setGender(src.getGender());
        dest.setDob(src.getDob());
        dest.setAddress(src.getAddress());
        dest.setRoles(src.getRoles());
    }

    @Transactional
    public User updateUser(User user)
    {
        if (!userRepository.findById(user.getId()).isPresent())
        {
            throw new ResourceNotFoundException("User not found with id: " + user.getId());
        }
        return userRepository.save(user);
    }

    @Transactional
    public User getUserById(Long id)
    {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public User getUserByEmail(String email)
    {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Transactional
    public User getUserByUsername(String username)
    {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    @Transactional
    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
