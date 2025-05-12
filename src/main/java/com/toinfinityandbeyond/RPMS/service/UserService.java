package com.toinfinityandbeyond.RPMS.service;

import com.toinfinityandbeyond.RPMS.exception.BadRequestException;
import com.toinfinityandbeyond.RPMS.exception.ResourceNotFoundException;
import com.toinfinityandbeyond.RPMS.model.User;
import com.toinfinityandbeyond.RPMS.repository.UserRepository;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User createUser(User user)
    {
        return userRepository.save(user);
    }

    @Transactional
    public User login(String username, String password) {
        User user = getUserByUsername(username);
        if (user.getPassword().equals(password)) {
            return user;  // Return the full user object
        } else {
            throw new BadRequestException("Invalid username or password");
        }
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
